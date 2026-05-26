import { api } from "./axiosConfig";
import type { AuthResponse, LoginRequest, RegisterRequest } from "../types/Auth";

export const authApi = {
  registrar: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>("/api/auth/register", data);
    return response.data;
  },

  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>("/api/auth/login", data);
    return response.data;
  },
};