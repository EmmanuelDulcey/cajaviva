export type LoginRequest = {
  email: string;
  password: string;
};

export type AuthResponse = {
  userId: string;
  email: string;
  roles: string[];
  expiresAt: string;
};

