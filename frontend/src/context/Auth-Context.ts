import { createContext } from "react";
import type { AuthResponse, LoginRequest, RegisterRequest } from "../types/Auth";

export interface AuthContextValue {
  user: AuthResponse | null;
  isAuthenticated: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextValue | undefined>(undefined);