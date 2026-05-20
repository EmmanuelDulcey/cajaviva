import { useState, type FormEvent } from 'react';
import eyePasswordIcon from '../assets/eye-password.svg';
import { useAuthSession } from '../session/AuthSessionContext';
import { toastPromise } from '../../../shared/toast/toastPromise';
import styles from './LoginForm.module.css';

type LoginFormProps = {
  onGoToRegister: () => void;
  onLoginSuccess: () => void;
};

export function LoginForm({ onGoToRegister, onLoginSuccess }: LoginFormProps) {
  const { login } = useAuthSession();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    try {
      setLoading(true);
      await toastPromise(
        login({
          email: email.trim(),
          password,
        }),
        {
          loading: 'Iniciando sesion...',
          successFallback: 'Sesion iniciada correctamente.',
          errorFallback: 'Credenciales invalidas. Intenta de nuevo.',
        },
      );
      onLoginSuccess();
    } catch {
      // Error message is handled by toast.promise
    } finally {
      setLoading(false);
    }
  };

  return (
    <form className={styles.form} onSubmit={handleSubmit} noValidate>
      <div className={styles.fieldGroup}>
        <label className={styles.label} htmlFor="email">
          Correo Electronico
        </label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(event) => setEmail(event.target.value)}
          placeholder="ejemplo@correo.com"
          autoComplete="email"
          required
        />
      </div>

      <div className={styles.fieldGroup}>
        <label className={styles.label} htmlFor="password">
          Contrasena
        </label>

        <div className={styles.passwordInputWrap}>
          <input
            id="password"
            type={showPassword ? 'text' : 'password'}
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            placeholder="••••••••"
            autoComplete="current-password"
            required
          />

          <button
            type="button"
            className={`${styles.eyeButton} ${showPassword ? styles.eyeButtonActive : ''}`}
            aria-label={showPassword ? 'Ocultar contrasena' : 'Mostrar contrasena'}
            onClick={() => setShowPassword((current) => !current)}
          >
            <img src={eyePasswordIcon} alt="" aria-hidden="true" />
          </button>
        </div>

        <button type="button" className={styles.forgotPassword}>
          ¿Olvidaste tu contrasena?
        </button>
      </div>

      <button className={styles.submitButton} type="submit" disabled={loading}>
        {loading ? 'Ingresando...' : 'Iniciar Sesion'}
      </button>

      <p className={styles.footerText}>
        ¿No tienes una cuenta?{' '}
        <button type="button" onClick={onGoToRegister}>
          Crear cuenta
        </button>
      </p>
    </form>
  );
}

