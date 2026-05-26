import { Link } from "react-router-dom";
import { BarChart3, Mic, SlidersHorizontal, Speaker } from "lucide-react";
import { useAuth } from "../context/useAuth";

export function DashboardPage() {
  const { user } = useAuth();

  return (
    <main className="dashboard">
      <section className="dashboard-header">
        <div>
          <h1>Bienvenido, {user?.nombre}</h1>
          <p>
            Desde aquí podrás gestionar tus dispositivos, crear mediciones y
            generar perfiles de ecualización.
          </p>
        </div>

        <Link to="/medicion-audio" className="btn btn-primary">
          Nueva medición
        </Link>
      </section>

      <section className="dashboard-grid">
        <article className="stat-card">
          <Speaker />
          <h3>Parlantes</h3>
          <p>Próximamente: registro y gestión de parlantes.</p>
        </article>

        <article className="stat-card">
          <Mic />
          <h3>Micrófonos</h3>
          <p>Próximamente: configuración de micrófono externo.</p>
        </article>

        <article className="stat-card">
          <BarChart3 />
          <h3>Mediciones</h3>
          <p>Captura datos acústicos usando Web Audio API.</p>
        </article>

        <article className="stat-card">
          <SlidersHorizontal />
          <h3>Perfiles EQ</h3>
          <p>Genera filtros seguros para Equalizer APO / Peace.</p>
        </article>
      </section>
    </main>
  );
}