import { useEffect, useState } from 'react';
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
import { Skeleton } from '../../../shared/components/skeleton/Skeleton';
import { CategoriesPage } from '../../categories/pages/CategoriesPage';
import { dashboardApi } from '../../dashboard/api/dashboardApi';
import type { DashboardSummary } from '../../dashboard/types/dashboard';
import styles from './AuthenticatedHomePage.module.css';

type AuthenticatedHomePageProps = {
  email: string;
  onLogout: () => void;
};

type AppSection = 'dashboard' | 'categories';

const formatCurrency = (amount: number, currency = 'COP') =>
  new Intl.NumberFormat('es-CO', {
    style: 'currency',
    currency,
    maximumFractionDigits: 0,
  }).format(amount);

function DashboardSkeleton() {
  return (
    <>
      <div className={styles.greeting}>
        <Skeleton className={styles.skeletonGreetingTitle} />
        <Skeleton className={styles.skeletonGreetingText} />
      </div>

      <section className={styles.grid} aria-label="Cargando dashboard">
        <article className={`${styles.card} ${styles.balanceCard}`}>
          <Skeleton className={styles.skeletonLabel} />
          <Skeleton className={styles.skeletonBalance} />
          <Skeleton className={styles.skeletonBalanceMeta} />
        </article>

        <article className={`${styles.card} ${styles.alertCard}`}>
          <Skeleton className={styles.skeletonIcon} />
          <Skeleton className={styles.skeletonAlertTitle} />
          <Skeleton className={styles.skeletonAlertText} />
          <Skeleton className={styles.skeletonButton} />
        </article>

        <div className={`${styles.card} ${styles.quickActions}`}>
          {Array.from({ length: 3 }).map((_, index) => (
            <Skeleton key={index} className={styles.skeletonQuickAction} />
          ))}
        </div>

        <article className={`${styles.card} ${styles.accountsCard}`}>
          <Skeleton className={styles.skeletonSectionTitle} />
          <div className={styles.accountsGrid}>
            {Array.from({ length: 2 }).map((_, index) => (
              <Skeleton key={index} className={styles.skeletonAccount} />
            ))}
          </div>
        </article>

        <article className={`${styles.card} ${styles.liquidityCard}`}>
          <Skeleton className={styles.skeletonSectionTitle} />
          <Skeleton className={styles.skeletonChart} />
          <Skeleton className={styles.skeletonTextLine} />
        </article>

        <article className={`${styles.card} ${styles.transactionsCard}`}>
          <Skeleton className={styles.skeletonSectionTitle} />
          {Array.from({ length: 3 }).map((_, index) => (
            <Skeleton key={index} className={styles.skeletonTransaction} />
          ))}
        </article>
      </section>
    </>
  );
}

export function AuthenticatedHomePage({ email, onLogout }: AuthenticatedHomePageProps) {
  const userInitials = email ? email.slice(0, 2).toUpperCase() : "";
  const [section, setSection] = useState<AppSection>('dashboard');
  const [dashboard, setDashboard] = useState<DashboardSummary | null>(null);
  const [dashboardLoading, setDashboardLoading] = useState(true);
  const [dashboardError, setDashboardError] = useState(false);

  useEffect(() => {
    let isMounted = true;

    const loadDashboard = async () => {
      try {
        setDashboardLoading(true);
        setDashboardError(false);
        const data = await dashboardApi.summary();
        if (isMounted) {
          setDashboard(data);
        }
      } catch {
        if (isMounted) {
          setDashboardError(true);
        }
      } finally {
        if (isMounted) {
          setDashboardLoading(false);
        }
      }
    };

    void loadDashboard();

    return () => {
      isMounted = false;
    };
  }, []);

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
          <button type="button" className={styles.navItem}>
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

        {section === 'dashboard' ? (
          <>
            {dashboardLoading ? <DashboardSkeleton /> : null}

            {!dashboardLoading && dashboardError ? (
              <section className={styles.emptyDashboard}>
                <h2>No pudimos cargar tu dashboard</h2>
                <p>Intenta actualizar la pagina en unos segundos.</p>
              </section>
            ) : null}

            {!dashboardLoading && dashboard ? (
              <>
                <div className={styles.greeting}>
                  <h2>Hola, {dashboard.greetingName}</h2>
                  <p>Aqui tienes el resumen de tu calma financiera.</p>
                </div>

                <section className={styles.grid}>
                  <article className={`${styles.card} ${styles.balanceCard}`}>
                    <div className={styles.balanceBlur} aria-hidden="true" />
                    <p className={styles.cardLabel}>BALANCE TOTAL DISPONIBLE</p>
                    <div className={styles.balanceLine}>
                      <strong>{formatCurrency(dashboard.totalBalance, dashboard.currency)}</strong>
                      <span>{dashboard.currency}</span>
                    </div>
                    <div className={styles.balanceMeta}>
                      <span className={styles.balanceTrend}>
                        <img src={balanceTrendIcon} alt="" aria-hidden="true" />
                        {dashboard.monthlyBalanceVariationPercent >= 0 ? '+' : ''}
                        {dashboard.monthlyBalanceVariationPercent}% este mes
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
                    <h3>{dashboard.nextAlert?.title ?? 'Sin alertas'}</h3>
                    <p>{dashboard.nextAlert?.message ?? 'No tienes alertas financieras pendientes.'}</p>
                    <button type="button" disabled={!dashboard.nextAlert}>
                      Revisar alerta
                    </button>
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
                    {dashboard.accounts.length > 0 ? (
                      <div className={styles.accountsGrid}>
                        {dashboard.accounts.map((account) => (
                          <div key={account.id} className={styles.accountItem}>
                            <p className={styles.accountTitle}>{account.name}</p>
                            <p className={styles.accountSubtitle}>{account.subtitle}</p>
                            <p className={styles.accountAmount}>
                              {formatCurrency(account.balance, dashboard.currency)}
                            </p>
                            {typeof account.progressPercentage === 'number' ? (
                              <div className={styles.progressTrack}>
                                <div
                                  style={{ width: `${account.progressPercentage}%` }}
                                  className={styles.progressFill}
                                />
                              </div>
                            ) : null}
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p className={styles.emptyState}>Aun no tienes cuentas registradas.</p>
                    )}
                  </article>

                  <article className={`${styles.card} ${styles.liquidityCard}`}>
                    <div className={styles.sectionHeader}>
                      <h3>Liquidez a 30 dias</h3>
                    </div>
                    <div className={styles.mockChart}>
                      {dashboard.liquidity.points.length > 0 ? (
                        <svg viewBox="0 0 100 40" preserveAspectRatio="none">
                          <polyline
                            points={dashboard.liquidity.points
                              .map((point, index, points) => {
                                const x = points.length === 1 ? 50 : (index / (points.length - 1)) * 100;
                                const max = Math.max(...points.map((item) => item.projectedBalance));
                                const min = Math.min(...points.map((item) => item.projectedBalance));
                                const range = max - min || 1;
                                const y = 34 - ((point.projectedBalance - min) / range) * 26;
                                return `${x},${y}`;
                              })
                              .join(' ')}
                            className={styles.chartLine}
                          />
                        </svg>
                      ) : (
                        <p className={styles.emptyChart}>Sin datos</p>
                      )}
                    </div>
                    <p>{dashboard.liquidity.message}</p>
                  </article>

                  <article className={`${styles.card} ${styles.transactionsCard}`}>
                    <div className={styles.sectionHeader}>
                      <h3>Ultimos Movimientos</h3>
                      <button type="button" className={styles.searchAction}>
                        Buscar <img src={searchIcon} alt="" aria-hidden="true" />
                      </button>
                    </div>
                    {dashboard.recentTransactions.length > 0 ? (
                      <div className={styles.transactionList}>
                        {dashboard.recentTransactions.map((transaction) => (
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
                            <p
                              className={`${styles.transactionAmount} ${
                                transaction.positive ? styles.positiveAmount : ''
                              }`}
                            >
                              {transaction.positive ? '+' : '-'}
                              {formatCurrency(transaction.amount, dashboard.currency)}
                            </p>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p className={styles.emptyState}>Aun no tienes movimientos registrados.</p>
                    )}
                  </article>
                </section>
              </>
            ) : null}
          </>
        ) : (
          <CategoriesPage />
        )}
      </section>
    </main>
  );
}

