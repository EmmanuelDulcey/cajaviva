import brandingBackground from '../../../assets/branding/branding-background.png';
import brandIcon from '../../../assets/branding/brand-Icon.svg';
import { RegisterForm } from '../components/RegisterForm';
import styles from './RegisterPage.module.css';

type RegisterPageProps = {
  onGoToLogin: () => void;
  onRegisterSuccess: () => void;
};

export function RegisterPage({ onGoToLogin, onRegisterSuccess }: RegisterPageProps) {
  return (
    <main className={styles.page}>
      <section className={styles.card} aria-label="Registro de usuario">
        <aside className={styles.hero} style={{ backgroundImage: `url(${brandingBackground})` }}>
          <div className={styles.logoContainer}>
            <img src={brandIcon} alt="" aria-hidden="true" />
            <span className={styles.logo}>CajaViva</span>
          </div>
          <div className={styles.heroTextBlock}>
            <h2>Comienza tu viaje a la calma financiera.</h2>
            <p>Unete a miles de usuarios que ya gestionan su futuro con tranquilidad.</p>
          </div>
        </aside>

        <section className={styles.formContainer}>
          <RegisterForm onGoToLogin={onGoToLogin} onRegisterSuccess={onRegisterSuccess} />
        </section>
      </section>
    </main>
  );
}

