import { useState } from "react";
import { useForm } from "react-hook-form";
import { useNavigate, Link } from "react-router-dom";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useAuth } from "../context/useAuth";
import axios from "axios";

const loginSchema = z.object({
  correo: z.string().email("Correo inválido"),
  password: z.string().min(1, "La contraseña es obligatoria"),
});

type LoginForm = z.infer<typeof loginSchema>;

export function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [error, setError] = useState("");

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = async (data: LoginForm) => {
    try {
      setError("");
      await login(data);
      navigate("/dashboard");
        } catch (err) {
      if (axios.isAxiosError(err)) {
        const backendError = err.response?.data;

        if (backendError?.error) {
          setError(backendError.error);
          return;
        }

        if (!err.response) {
          setError("No hay conexión con el backend. Revisa que Spring Boot esté corriendo en localhost:8080.");
          return;
        }

        setError(`Error ${err.response.status}: no fue posible iniciar sesión.`);
        return;
      }

      setError("Ocurrió un error inesperado al iniciar sesión.");
    }
  };

  return (
    <main className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit(onSubmit)}>
        <h1>Iniciar sesión</h1>
        <p>Ingresa para acceder a tus mediciones y perfiles acústicos.</p>

        {error && <div className="alert">{error}</div>}

        <label>
          Correo
          <input type="email" placeholder="correo@ejemplo.com" {...register("correo")} />
          {errors.correo && <span className="error">{errors.correo.message}</span>}
        </label>

        <label>
          Contraseña
          <input type="password" placeholder="Tu contraseña" {...register("password")} />
          {errors.password && <span className="error">{errors.password.message}</span>}
        </label>

        <button className="btn btn-primary full" disabled={isSubmitting}>
          {isSubmitting ? "Ingresando..." : "Ingresar"}
        </button>

        <p className="small-text">
          ¿No tienes cuenta? <Link to="/registro">Crear cuenta</Link>
        </p>
      </form>
    </main>
  );
}