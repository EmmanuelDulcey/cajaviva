import { useMemo, useState, type ChangeEvent, type FormEvent } from 'react';
import eyePasswordIcon from '../assets/eye-password.svg';
import { registerUser } from '../api/registerUser';
import { useAuthSession } from '../session/AuthSessionContext';
import type { RegisterUserRequest } from '../types/register';
import { toastPromise } from '../../../shared/toast/toastPromise';
import styles from './RegisterForm.module.css';

type RegisterFormProps = {
  onGoToLogin: () => void;
  onRegisterSuccess: () => void;
};

type RegisterFormErrors = {
  name?: string;
  lastName?: string;
  email?: string;
  password?: string;
};

const initialForm: RegisterUserRequest = {
  name: '',
  lastName: '',
  email: '',
  password: '',
};

const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

const validateForm = (form: RegisterUserRequest): RegisterFormErrors => {
  const errors: RegisterFormErrors = {};

  if (!form.name.trim()) {
    errors.name = 'El nombre es obligatorio.';
  }

  if (!form.lastName.trim()) {
    errors.lastName = 'El apellido es obligatorio.';
  }

  if (!form.email.trim()) {
    errors.email = 'El correo electronico es obligatorio.';
  } else if (!emailRegex.test(form.email.trim())) {
    errors.email = 'Ingresa un correo electronico valido.';
  }

  if (!form.password) {
    errors.password = 'La contrasena es obligatoria.';
  } else if (form.password.length < 8) {
    errors.password = 'Debe contener al menos 8 caracteres.';
  }

  return errors;
};

export function RegisterForm({ onGoToLogin, onRegisterSuccess }: RegisterFormProps) {
  const { login } = useAuthSession();
  const [form, setForm] = useState(initialForm);
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [touched, setTouched] = useState<Record<keyof RegisterUserRequest, boolean>>({
    name: false,
    lastName: false,
    email: false,
    password: false,
  });

  const errors = useMemo(() => validateForm(form), [form]);

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
    setTouched((current) => ({ ...current, [name]: true }));
  };

  const handleBlur = (event: ChangeEvent<HTMLInputElement>) => {
    const { name } = event.target;
    setTouched((current) => ({ ...current, [name]: true }));
  };

  const hasErrors = Object.keys(errors).length > 0;

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setTouched({
      name: true,
      lastName: true,
      email: true,
      password: true,
    });

    if (hasErrors) {
      return;
    }

    const payload = {
      name: form.name.trim(),
      lastName: form.lastName.trim(),
      email: form.email.trim(),
      password: form.password,
    };

    try {
      setLoading(true);
      await toastPromise(
        (async () => {
          await registerUser(payload);
          await login({
            email: payload.email,
            password: payload.password,
          });
        })(),
        {
          loading: 'Creando cuenta...',
          successFallback: 'Cuenta creada e inicio de sesion exitoso.',
          errorFallback: 'No fue posible completar el registro. Verifica los datos e intenta de nuevo.',
        },
      );
      setForm(initialForm);
      onRegisterSuccess();
    } catch {
      // Errors are handled by toast.promise
    } finally {
      setLoading(false);
    }
  };

  const getFieldClassName = (field: keyof RegisterUserRequest) =>
    `${styles.input} ${touched[field] && errors[field] ? styles.inputError : ''}`;

  return (
    <form className={styles.form} onSubmit={handleSubmit} noValidate>
      <h1 className={styles.title}>Crear cuenta</h1>

      <div className={styles.row}>
        <label className={styles.field}>
          <span className={styles.label}>Nombre</span>
          <input
            className={getFieldClassName('name')}
            name="name"
            value={form.name}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="Ej. Ana"
            required
            autoComplete="given-name"
          />
          {touched.name && errors.name ? <span className={styles.fieldError}>{errors.name}</span> : null}
        </label>

        <label className={styles.field}>
          <span className={styles.label}>Apellido</span>
          <input
            className={getFieldClassName('lastName')}
            name="lastName"
            value={form.lastName}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="Ej. Garcia"
            required
            autoComplete="family-name"
          />
          {touched.lastName && errors.lastName ? (
            <span className={styles.fieldError}>{errors.lastName}</span>
          ) : null}
        </label>
      </div>

      <label className={styles.field}>
        <span className={styles.label}>Correo electronico</span>
        <input
          className={getFieldClassName('email')}
          type="email"
          name="email"
          value={form.email}
          onChange={handleChange}
          onBlur={handleBlur}
          placeholder="tucorreo@ejemplo.com"
          required
          autoComplete="email"
        />
        {touched.email && errors.email ? <span className={styles.fieldError}>{errors.email}</span> : null}
      </label>

      <label className={styles.field}>
        <span className={styles.label}>Contrasena</span>
        <div
          className={`${styles.passwordInputWrap} ${
            touched.password && errors.password ? styles.passwordInputWrapError : ''
          }`}
        >
          <input
            className={`${styles.passwordInput} ${
              touched.password && errors.password ? styles.inputError : ''
            }`}
            type={showPassword ? 'text' : 'password'}
            name="password"
            value={form.password}
            onChange={handleChange}
            onBlur={handleBlur}
            placeholder="Minimo 8 caracteres"
            required
            minLength={8}
            autoComplete="new-password"
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
        {touched.password && errors.password ? (
          <span className={styles.fieldError}>{errors.password}</span>
        ) : null}
      </label>

      <button className={styles.submitButton} type="submit" disabled={loading}>
        {loading ? 'Creando...' : 'Continuar'}
      </button>

      <p className={styles.footerText}>
        Ya tienes una cuenta?{' '}
        <button type="button" onClick={onGoToLogin}>
          Inicia sesion
        </button>
      </p>
    </form>
  );
}

