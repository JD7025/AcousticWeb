import { Link } from "react-router-dom";
import { BarChart3, Mic, SlidersHorizontal, Volume2 } from "lucide-react";

export function LandingPage() {
  return (
    <main className="landing">
      <section className="hero">
        <div className="hero-content">
          <span className="badge">Optimización acústica web</span>

          <h1>Calibra tu parlante usando un micrófono externo</h1>

          <p>
            AcousticWeb permite medir la respuesta aproximada de un parlante,
            analizar su comportamiento acústico y generar perfiles de
            ecualización seguros para mejorar su sonido.
          </p>

          <div className="hero-actions">
            <Link to="/registro" className="btn btn-primary">
              Comenzar ahora
            </Link>

            <Link to="/login" className="btn btn-secondary">
              Iniciar sesión
            </Link>
          </div>
        </div>

        <div className="hero-card">
          <Volume2 size={48} />
          <h3>Flujo de calibración</h3>
          <ol>
            <li>Registra tu parlante, micrófono y sala.</li>
            <li>Mide ruido ambiente.</li>
            <li>Reproduce señal de prueba.</li>
            <li>Captura respuesta con Web Audio API.</li>
            <li>Genera un perfil EQ seguro.</li>
          </ol>
        </div>
      </section>

      <section className="features">
        <article className="feature-card">
          <Mic />
          <h3>Micrófono externo</h3>
          <p>
            La aplicación solicita permiso del navegador para capturar audio y
            analizar la respuesta acústica.
          </p>
        </article>

        <article className="feature-card">
          <BarChart3 />
          <h3>Gráficas acústicas</h3>
          <p>
            Visualiza datos de frecuencia, nivel en dB y comportamiento del
            parlante en la sala.
          </p>
        </article>

        <article className="feature-card">
          <SlidersHorizontal />
          <h3>Perfiles EQ</h3>
          <p>
            Genera filtros paramétricos conservadores para evitar clipping o
            deterioro del sonido.
          </p>
        </article>
      </section>

      <section className="limited-section">
        <h2>Funciones limitadas sin iniciar sesión</h2>
        <p>
          Puedes conocer el proyecto y revisar el flujo general. Para medir,
          guardar datos, generar perfiles y exportar filtros debes iniciar sesión.
        </p>
      </section>
    </main>
  );
}