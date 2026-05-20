export const AUTH_ROUTES = {
  login: '/',
  register: '/register',
  app: '/app',
} as const;

export type AuthRoute = (typeof AUTH_ROUTES)[keyof typeof AUTH_ROUTES];

export const getAuthRoute = (pathname: string): AuthRoute => {
  if (pathname === AUTH_ROUTES.app) {
    return AUTH_ROUTES.app;
  }
  if (pathname === AUTH_ROUTES.register) {
    return AUTH_ROUTES.register;
  }
  return AUTH_ROUTES.login;
};

