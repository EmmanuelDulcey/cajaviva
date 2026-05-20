import { authApi } from '../api/authApi';
import type { AuthResponse, LoginRequest } from '../types/login';

const tryGetCurrentUser = async (): Promise<AuthResponse | null> => {
  try {
    return await authApi.me();
  } catch {
    return null;
  }
};

export const authSessionService = {
  async bootstrap(): Promise<AuthResponse | null> {
    const currentUser = await tryGetCurrentUser();
    if (currentUser) {
      return currentUser;
    }

    try {
      await authApi.refresh();
      return await tryGetCurrentUser();
    } catch {
      return null;
    }
  },

  async login(payload: LoginRequest): Promise<AuthResponse> {
    await authApi.login(payload);
    return authApi.me();
  },

  async logout(): Promise<void> {
    try {
      await authApi.logout();
    } catch {
      // Even if API fails, local session should be cleared.
    }
  },
};

