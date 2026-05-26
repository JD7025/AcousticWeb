import { Link, useNavigate } from "react-router-dom";
import { Activity, LogOut, User } from "lucide-react";
import { useAuth } from "../context/useAuth";

export function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <header className="navbar">
      <Link to="/" className="brand">
        <Activity size={26} />
        <span>AcousticWeb</span>
      </Link>

      <nav className="nav-links">
        <Link to="/">Inicio</Link>

        {isAuthenticated && <Link to="/dashboard">Dashboard</Link>}
        {isAuthenticated && <Link to="/medicion-audio">Medición Audio</Link>}

        {!isAuthenticated && <Link to="/login">Iniciar sesión</Link>}
        {!isAuthenticated && <Link to="/registro" className="btn btn-primary">Registrarse</Link>}

        {isAuthenticated && (
          <div className="user-box">
            <User size={18} />
            <span>{user?.nombre}</span>
            <button className="btn btn-secondary" onClick={handleLogout}>
              <LogOut size={16} />
              Salir
            </button>
          </div>
        )}
      </nav>
    </header>
  );
}