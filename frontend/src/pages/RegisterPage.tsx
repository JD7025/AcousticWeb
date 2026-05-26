import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, Link } from "react-router-dom";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuth } from "../context/useAuth";
import axios from "axios";

const registroSchema = z
  .object({
    nombre: z.string().min(3, "El nombre debe tener mínimo 3 caracteres"),
    correo: z.string().email("Correo inválido"),
    password: z
      .string()
      .min(8, "La contraseña debe tener mínimo 8 caracteres")
      .regex(/[A-Z]/, "Debe incluir una mayúscula")
      .regex(/[a-z]/, "Debe incluir una minúscula")
      .regex(/[0-9]/, "Debe incluir un número"),
    confirmarPassword: z.string(),
    aceptaTerminos: z.boolean().refine((value) => value, {
      message: "Debes aceptar los términos de uso académico",
    }),
  })
  .refine((data) => data.password === data.confirmarPassword, {
    message: "Las contraseñas no coinciden",
    path: ["confirmarPassword"],
  });

type RegistroForm = z.infer<typeof registroSchema>;

export function RegisterPage() {
  const { register: registerUser } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegistroForm>({
    resolver: zodResolver(registroSchema),
    defaultValues: {
      aceptaTerminos: false,
    },
  });

  const onSubmit = async (data: RegistroForm) => {
    try {
      setError("");

      await registerUser({
        nombre: data.nombre,
        correo: data.correo,
        password: data.password,
      });

      navigate("/dashboard");
    } catch (err) {
      if (axios.isAxiosError(err)) {
        const backendError = err.response?.data;

        if (backendError?.error) {
          setError(backendError.error);
          return;
        }

        if (backendError?.detalles) {
          const detalles = Object.values(backendError.detalles).join(" | ");
          setError(detalles);
          return;
        }

        if (!err.response) {
          setError("No hay conexión con el backend. Revisa que Spring Boot esté corriendo en localhost:8080.");
          return;
        }

        setError(`Error ${err.response.status}: no fue posible registrar el usuario.`);
        return;
      }

      setError("Ocurrió un error inesperado al registrar el usuario.");
    }
  };

  return (
    <main className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit(onSubmit)}>
        <h1>Crear cuenta</h1>
        <p>Regístrate para guardar mediciones y perfiles de calibración.</p>

        {error && <div className="alert">{error}</div>}

        <label>
          Nombre
          <input placeholder="Tu nombre" {...register("nombre")} />
          {errors.nombre && <span className="error">{errors.nombre.message}</span>}
        </label>

        <label>
          Correo
          <input type="email" placeholder="correo@ejemplo.com" {...register("correo")} />
          {errors.correo && <span className="error">{errors.correo.message}</span>}
        </label>

        <label>
          Contraseña
          <input type="password" placeholder="Mínimo 8 caracteres" {...register("password")} />
          {errors.password && <span className="error">{errors.password.message}</span>}
        </label>

        <label>
          Confirmar contraseña
          <input type="password" placeholder="Repite la contraseña" {...register("confirmarPassword")} />
          {errors.confirmarPassword && (
            <span className="error">{errors.confirmarPassword.message}</span>
          )}
        </label>

        <label className="checkbox-label">
          <input type="checkbox" {...register("aceptaTerminos")} />
          Acepto el uso académico de la plataforma y entiendo que la medición es aproximada.
        </label>
        {errors.aceptaTerminos && (
          <span className="error">{errors.aceptaTerminos.message}</span>
        )}

        <button className="btn btn-primary full" disabled={isSubmitting}>
          {isSubmitting ? "Registrando..." : "Crear cuenta"}
        </button>

        <p className="small-text">
          ¿Ya tienes cuenta? <Link to="/login">Iniciar sesión</Link>
        </p>
      </form>
    </main>
  );
}