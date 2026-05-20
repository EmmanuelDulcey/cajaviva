import { useEffect, useMemo, useState } from 'react';
import { getAuthRoute, AUTH_ROUTES, type AuthRoute } from './features/auth/authRoutes';
import { LoginPage } from './features/auth/pages/LoginPage';
import { RegisterPage } from './features/auth/pages/RegisterPage';
import { AuthenticatedHomePage } from './features/auth/pages/AuthenticatedHomePage';
import { AuthSessionProvider, useAuthSession } from './features/auth/session/AuthSessionContext';

function AppRoutes() {
  const { isAuthenticated, isBootstrapping, user, logout } = useAuthSession();
  const [currentRoute, setCurrentRoute] = useState<AuthRoute>(() =>
    getAuthRoute(window.location.pathname),
  );
  const [shouldAnimateLoginIntro, setShouldAnimateLoginIntro] = useState<boolean>(() =>
    getAuthRoute(window.location.pathname) === AUTH_ROUTES.login,
  );

  useEffect(() => {
    const handlePopState = () => {
      setCurrentRoute(getAuthRoute(window.location.pathname));
    };

    window.addEventListener('popstate', handlePopState);
    return () => window.removeEventListener('popstate', handlePopState);
  }, []);

  const navigateTo = (route: AuthRoute) => {
    if (window.location.pathname !== route) {
      window.history.pushState({}, '', route);
    }
    setCurrentRoute(route);
  };

  useEffect(() => {
    if (isBootstrapping) {
      return;
    }

    if (isAuthenticated && currentRoute !== AUTH_ROUTES.app) {
      navigateTo(AUTH_ROUTES.app);
      return;
    }

    if (!isAuthenticated && currentRoute === AUTH_ROUTES.app) {
      navigateTo(AUTH_ROUTES.login);
    }
  }, [currentRoute, isAuthenticated, isBootstrapping]);

  const page = useMemo(() => {
    if (isBootstrapping) {
      return null;
    }

    if (isAuthenticated && user) {
      return (
        <AuthenticatedHomePage
          email={user.email}
          onLogout={async () => {
            await logout();
            setShouldAnimateLoginIntro(true);
            navigateTo(AUTH_ROUTES.login);
          }}
        />
      );
    }

    if (currentRoute === AUTH_ROUTES.register) {
      return (
        <RegisterPage
          onGoToLogin={() => navigateTo(AUTH_ROUTES.login)}
          onRegisterSuccess={() => navigateTo(AUTH_ROUTES.app)}
        />
      );
    }

    return (
      <LoginPage
        animateOnMount={shouldAnimateLoginIntro}
        onIntroAnimationConsumed={() => setShouldAnimateLoginIntro(false)}
        onGoToRegister={() => navigateTo(AUTH_ROUTES.register)}
        onLoginSuccess={() => navigateTo(AUTH_ROUTES.app)}
      />
    );
  }, [currentRoute, isAuthenticated, isBootstrapping, logout, shouldAnimateLoginIntro, user]);

  return page;
}

function App() {
  return (
    <AuthSessionProvider>
      <AppRoutes />
    </AuthSessionProvider>
  );
}

export default App;


