import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type PropsWithChildren,
} from 'react';
import { authSessionService } from './authSessionService';
import type { AuthResponse, LoginRequest } from '../types/login';

type AuthSessionContextValue = {
  user: AuthResponse | null;
  isAuthenticated: boolean;
  isBootstrapping: boolean;
  login: (payload: LoginRequest) => Promise<void>;
  logout: () => Promise<void>;
};

const AuthSessionContext = createContext<AuthSessionContextValue | undefined>(undefined);

export function AuthSessionProvider({ children }: PropsWithChildren) {
  const [user, setUser] = useState<AuthResponse | null>(null);
  const [isBootstrapping, setIsBootstrapping] = useState(true);

  useEffect(() => {
    let cancelled = false;

    const bootstrap = async () => {
      const currentUser = await authSessionService.bootstrap();
      if (!cancelled) {
        setUser(currentUser);
        setIsBootstrapping(false);
      }
    };

    void bootstrap();

    return () => {
      cancelled = true;
    };
  }, []);

  const login = useCallback(async (payload: LoginRequest) => {
    const loggedInUser = await authSessionService.login(payload);
    setUser(loggedInUser);
  }, []);

  const logout = useCallback(async () => {
    await authSessionService.logout();
    setUser(null);
  }, []);

  const value = useMemo<AuthSessionContextValue>(
    () => ({
      user,
      isAuthenticated: user !== null,
      isBootstrapping,
      login,
      logout,
    }),
    [isBootstrapping, login, logout, user],
  );

  return <AuthSessionContext.Provider value={value}>{children}</AuthSessionContext.Provider>;
}

export function useAuthSession() {
  const context = useContext(AuthSessionContext);
  if (!context) {
    throw new Error('useAuthSession must be used inside AuthSessionProvider');
  }
  return context;
}

