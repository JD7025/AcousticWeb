import { /*useEffect,*/ useState, type ReactNode } from "react";
import type { AuthResponse, LoginRequest, RegisterRequest } from "../types/Auth";
import { authApi } from "../api/authApi";
import { AuthContext } from "./Auth-Context";

const STORAGE_KEY = "acousticweb_user";

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthResponse | null>(() => {
    const savedUser = localStorage.getItem(STORAGE_KEY);

    if (!savedUser) return null;

    try {
      return JSON.parse(savedUser) as AuthResponse;
    } catch {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }
  });

  const login = async (data: LoginRequest) => {
    const response = await authApi.login(data);
    setUser(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
  };

  const register = async (data: RegisterRequest) => {
    const response = await authApi.registrar(data);
    setUser(response);
    localStorage.setItem(STORAGE_KEY, JSON.stringify(response));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem(STORAGE_KEY);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: Boolean(user),
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}