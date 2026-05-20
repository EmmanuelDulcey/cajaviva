import client from '../../../api/client';
import type { AuthResponse, LoginRequest } from '../types/login';

export const authApi = {
  login: async (payload: LoginRequest): Promise<AuthResponse> => {
    const response = await client.post<AuthResponse>('/auth/login', payload);
    return response.data;
  },

  me: async (): Promise<AuthResponse> => {
    const response = await client.get<AuthResponse>('/auth/me');
    return response.data;
  },

  refresh: async (): Promise<AuthResponse> => {
    const response = await client.post<AuthResponse>('/auth/refresh');
    return response.data;
  },

  logout: async (): Promise<void> => {
    await client.post('/auth/logout');
  },
};

