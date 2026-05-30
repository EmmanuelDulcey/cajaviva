import { useState } from 'react';
import brandIcon from '../../../assets/branding/brand-Icon.svg';
import addAccountIcon from '../../../assets/dashboard/add-account-icon.svg';
import categoriesIcon from '../../../assets/dashboard/categories-icon.svg';
import balanceChevronIcon from '../../../assets/dashboard/balance-chevron-icon.svg';
import balanceTrendIcon from '../../../assets/dashboard/balance-trend-icon.svg';
import nextPaymentIcon from '../../../assets/dashboard/next-payment-icon.svg';
import notificationsIcon from '../../../assets/dashboard/notifications-icon.svg';
import searchIcon from '../../../assets/dashboard/search-icon.svg';
import transferIcon from '../../../assets/dashboard/transfer-icon.svg';
import walletIcon from '../../../assets/dashboard/wallet-icon.svg';
import accountsSidebarIcon from '../../../assets/sidebar/accounts-icon.svg';
import configSidebarIcon from '../../../assets/sidebar/config-icon.svg';
import dashboardSidebarIcon from '../../../assets/sidebar/dashboard-icon.svg';
import logoutSidebarIcon from '../../../assets/sidebar/logout-icon.svg';
import profileSidebarIcon from '../../../assets/sidebar/profile-icon.svg';
import projectionsSidebarIcon from '../../../assets/sidebar/projections-icon.svg';
import transactionsSidebarIcon from '../../../assets/sidebar/transactions-icon.svg';
import { CategoriesPage } from '../../categories/pages/CategoriesPage';
import { ProjectionsPage } from '../../projections/pages/ProjectionsPage';
import styles from './AuthenticatedHomePage.module.css';

type AuthenticatedHomePageProps = {
  email: string;
  onLogout: () => void;
};

type AppSection = 'dashboard' | 'categories' | 'projections';

type AccountItem = {
  id: string;
  title: string;
  subtitle: string;
  amount: string;
  progress?: number;
};

type TransactionItem = {
  id: string;
  title: string;
  subtitle: string;
  amount: string;
  positive?: boolean;
};

const accounts: AccountItem[] = [
  { id: 'debit', title: 'Debito Principal', subtitle: '*** 4589', amount: '$120,000.00' },
  {
    id: 'savings',
    title: 'Fondo Emergencia',
    subtitle: 'Meta: $50,000.00',
    amount: '$22,500.00',
    progress: 45,
  },
];

const transactions: TransactionItem[] = [
  { id: '1', title: 'Supermercado El Sol', subtitle: 'Hoy, 14:30 · Tarjeta ****4589', amount: '-$1,250.00' },
  {
    id: '2',
    title: 'Nomina Quincenal',
    subtitle: 'Ayer · Transferencia SPEI',
    amount: '+$15,000.00',
    positive: true,
  },
  { id: '3', title: 'Pago de Luz CFE', subtitle: '12 Mar · Debito Automatico', amount: '-$450.00' },
];

export function AuthenticatedHomePage({ email, onLogout }: AuthenticatedHomePageProps) {
  const userInitials = email ? email.slice(0, 2).toUpperCase() : "";
  const [section, setSection] = useState<AppSection>('dashboard');

  return (
    <main className={styles.page}>
      <aside className={styles.sidebar}>
        <div className={styles.brandBlock}>
          <span className={styles.brandIconWrap}>
            <img src={brandIcon} alt="" aria-hidden="true" />
          </span>
          <h1>CajaViva</h1>
          <p>Calma Financiera</p>
        </div>

        <nav className={styles.nav}>
          <button
            type="button"
            className={`${styles.navItem} ${section === 'dashboard' ? styles.navItemActive : ''}`}
            onClick={() => setSection('dashboard')}
          >
            <img src={dashboardSidebarIcon} alt="" aria-hidden="true" />
            Inicio
          </button>
          <button type="button" className={styles.navItem}>
            <img src={accountsSidebarIcon} alt="" aria-hidden="true" />
            Cuentas
          </button>
          <button type="button" className={styles.navItem}>
            <img src={transactionsSidebarIcon} alt="" aria-hidden="true" />
            Transacciones
          </button>
          <button
            type="button"
            className={`${styles.navItem} ${section === 'projections' ? styles.navItemActive : ''}`}
            onClick={() => setSection('projections')}
          >
            <img src={projectionsSidebarIcon} alt="" aria-hidden="true" />
            Proyecciones
          </button>
          <button
            type="button"
            className={`${styles.navItem} ${section === 'categories' ? styles.navItemActive : ''}`}
            onClick={() => setSection('categories')}
          >
            <img src={categoriesIcon} alt="" aria-hidden="true" />
            Categorias
          </button>
          <button type="button" className={styles.navItem}>
            <img src={profileSidebarIcon} alt="" aria-hidden="true" />
            Perfil
          </button>
        </nav>

        <button type="button" className={styles.newTransactionButton}>
          <img src={transferIcon} alt="" aria-hidden="true" />
          Nueva Transaccion
        </button>

        <div className={styles.sidebarFooter}>
          <button type="button" className={styles.footerLink}>
            <img src={configSidebarIcon} alt="" aria-hidden="true" />
            Ajustes
          </button>
          <button type="button" className={styles.footerLink} onClick={onLogout}>
            <img src={logoutSidebarIcon} alt="" aria-hidden="true" />
            Cerrar Sesion
          </button>
        </div>
      </aside>

      <section className={styles.content}>
        <header className={styles.topbar}>
          <button type="button" aria-label="Notificaciones" className={styles.topIconButton}>
            <img src={notificationsIcon} alt="" aria-hidden="true" />
          </button>
          <button type="button" aria-label="Buscar" className={styles.topIconButton}>
            <img src={searchIcon} alt="" aria-hidden="true" />
          </button>
          <span className={styles.avatar}>{userInitials}</span>
        </header>

        {section === 'projections' ? (
          <ProjectionsPage />
        ) : section === 'categories' ? (
          <CategoriesPage />
        ) : (
          <>
            <div className={styles.greeting}>
              <h2>Hola, Ana</h2>
              <p>Aqui tienes el resumen de tu calma financiera.</p>
            </div>

            <section className={styles.grid}>
              <article className={`${styles.card} ${styles.balanceCard}`}>
                <div className={styles.balanceBlur} aria-hidden="true" />
                <p className={styles.cardLabel}>BALANCE TOTAL DISPONIBLE</p>
                <div className={styles.balanceLine}>
                  <strong>$142,500.00</strong>
                  <span>MXN</span>
                </div>
                <div className={styles.balanceMeta}>
                  <span className={styles.balanceTrend}>
                    <img src={balanceTrendIcon} alt="" aria-hidden="true" />
                    +4.2% este mes
                  </span>
                  <span className={styles.balanceDivider} aria-hidden="true" />
                  <button type="button" className={styles.balanceAnalysisButton}>
                    Ver analisis
                    <img src={balanceChevronIcon} alt="" aria-hidden="true" />
                  </button>
                </div>
              </article>

              <article className={`${styles.card} ${styles.alertCard}`}>
                <div className={styles.alertHeader}>
                  <span className={styles.alertIconWrap}>
                    <img src={nextPaymentIcon} alt="" aria-hidden="true" />
                  </span>
                  <span className={styles.alertChip}>Alerta</span>
                </div>
                <h3>Pago Proximo</h3>
                <p>Tu tarjeta de credito vence en 3 dias. Tienes saldo suficiente para el pago.</p>
                <button type="button">Programar Pago</button>
              </article>

              <div className={`${styles.card} ${styles.quickActions}`}>
                <button type="button" className={styles.quickButton}>
                  <span className={styles.quickIconWrap}>
                    <img src={transferIcon} alt="" aria-hidden="true" />
                  </span>
                  Transferir
                </button>
                <button type="button" className={styles.quickButton}>
                  <span className={styles.quickIconWrap}>
                    <img src={addAccountIcon} alt="" aria-hidden="true" />
                  </span>
                  Anadir Cuenta
                </button>
                <button type="button" className={styles.quickButton} onClick={() => setSection('categories')}>
                  <span className={styles.quickIconWrap}>
                    <img src={categoriesIcon} alt="" aria-hidden="true" />
                  </span>
                  Categorias
                </button>
              </div>

              <article className={`${styles.card} ${styles.accountsCard}`}>
                <div className={styles.sectionHeader}>
                  <h3>Mis Cuentas</h3>
                  <button type="button">Ver todas</button>
                </div>
                <div className={styles.accountsGrid}>
                  {accounts.map((account) => (
                    <div key={account.id} className={styles.accountItem}>
                      <p className={styles.accountTitle}>{account.title}</p>
                      <p className={styles.accountSubtitle}>{account.subtitle}</p>
                      <p className={styles.accountAmount}>{account.amount}</p>
                      {typeof account.progress === 'number' ? (
                        <div className={styles.progressTrack}>
                          <div style={{ width: `${account.progress}%` }} className={styles.progressFill} />
                        </div>
                      ) : null}
                    </div>
                  ))}
                </div>
              </article>

              <article className={`${styles.card} ${styles.liquidityCard}`}>
                <div className={styles.sectionHeader}>
                  <h3>Liquidez a 30 dias</h3>
                </div>
                <div className={styles.mockChart}>
                  <svg viewBox="0 0 100 40" preserveAspectRatio="none">
                    <polyline points="0,22 18,19 35,25 55,15 72,20 100,12" className={styles.chartLine} />
                  </svg>
                </div>
                <p>Flujo proyectado estable. Sin alertas de sobregiro.</p>
              </article>

              <article className={`${styles.card} ${styles.transactionsCard}`}>
                <div className={styles.sectionHeader}>
                  <h3>Ultimos Movimientos</h3>
                  <button type="button" className={styles.searchAction}>
                    Buscar <img src={searchIcon} alt="" aria-hidden="true" />
                  </button>
                </div>
                <div className={styles.transactionList}>
                  {transactions.map((transaction) => (
                    <div key={transaction.id} className={styles.transactionRow}>
                      <div className={styles.transactionIdentity}>
                        <span className={styles.transactionIconWrap}>
                          <img src={walletIcon} alt="" aria-hidden="true" />
                        </span>
                        <div>
                          <p className={styles.transactionTitle}>{transaction.title}</p>
                          <p className={styles.transactionSubtitle}>{transaction.subtitle}</p>
                        </div>
                      </div>
                      <p className={`${styles.transactionAmount} ${transaction.positive ? styles.positiveAmount : ''}`}>
                        {transaction.amount}
                      </p>
                    </div>
                  ))}
                </div>
              </article>
            </section>
          </>
        )}
      </section>
    </main>
  );
}

