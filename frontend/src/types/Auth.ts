export interface AuthResponse {
  id: number;
  nombre: string;
  correo: string;
  rol: string;
}


export interface RegisterRequest {
  nombre: string;
  correo: string;
  password: string;
}

export interface LoginRequest {
  correo: string;
  password: string;
}