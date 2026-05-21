import { useEffect, useState } from 'react';
import brandingBackground from '../../../assets/branding/branding-background.png';
import brandIcon from '../../../assets/branding/brand-Icon.svg';
import brandIconLogin from '../assets/brand-icon-login.svg';
import { LoginForm } from '../components/LoginForm';
import styles from './LoginPage.module.css';

type LoginPageProps = {
  animateOnMount: boolean;
  onIntroAnimationConsumed: () => void;
  onGoToRegister: () => void;
  onLoginSuccess: () => void;
};

export function LoginPage({
  animateOnMount,
  onIntroAnimationConsumed,
  onGoToRegister,
  onLoginSuccess,
}: LoginPageProps) {
  console.log("LoginPage renderizado ✅"); // <-- agregado

  const [showBanner, setShowBanner] = useState(!animateOnMount);

  useEffect(() => {
    if (!animateOnMount) {
      setShowBanner(true);
      return;
    }

    const timeoutId = window.setTimeout(() => {
      setShowBanner(true);
      onIntroAnimationConsumed();
    }, 220);

    return () => {
      window.clearTimeout(timeoutId);
    };
  }, [animateOnMount, onIntroAnimationConsumed]);

  return (
    <main className={styles.page}>
      <section className={styles.shell} aria-label="Inicio de sesion">
        <div
          className={`${styles.viewport} ${showBanner ? styles.viewportExpanded : styles.viewportCollapsed}`}
        >
          <aside
            className={`${styles.hero} ${showBanner ? styles.heroVisible : styles.heroHidden}`}
            style={{ backgroundImage: `url(${brandingBackground})` }}
          >
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
            <div className={styles.formInner}>
              <header className={styles.header}>
                <img src={brandIconLogin} alt="" aria-hidden="true" />
                <h1>CajaViva</h1>
                <p>Calma Financiera</p>
              </header>
              <LoginForm onGoToRegister={onGoToRegister} onLoginSuccess={onLoginSuccess} />
            </div>
          </section>
        </div>
      </section>
    </main>
  );
}
