import { useEffect, useMemo, useState, type FormEvent } from 'react';
import { Skeleton } from '../../../shared/components/skeleton/Skeleton';
import { toastPromise } from '../../../shared/toast/toastPromise';
import { projectionsApi } from '../api/projectionsApi';
import type { LiquidityProjection, LiquidityProjectionRequest } from '../types/projection';
import styles from './ProjectionsPage.module.css';

type CalculateForm = {
  accountId: string;
  startDate: string;
  endDate: string;
};

const initialForm: CalculateForm = {
  accountId: '',
  startDate: '',
  endDate: '',
};

function ProjectionsSkeleton() {
  return (
    <div className={styles.tableWrap}>
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} className={styles.skeletonRow}>
          <Skeleton className={styles.skeletonCell} />
          <Skeleton className={styles.skeletonCell} />
          <Skeleton className={styles.skeletonCell} />
        </div>
      ))}
    </div>
  );
}

function formatCurrency(amount: number): string {
  return new Intl.NumberFormat('es-MX', {
    style: 'currency',
    currency: 'MXN',
    minimumFractionDigits: 2,
  }).format(amount);
}

function formatDate(dateStr: string): string {
  const d = new Date(dateStr);
  return d.toLocaleDateString('es-MX', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  });
}

export function ProjectionsPage() {
  const [loading, setLoading] = useState(true);
  const [projections, setProjections] = useState<LiquidityProjection[]>([]);
  const [calculating, setCalculating] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState<CalculateForm>(initialForm);

  const loadProjections = async () => {
    try {
      setLoading(true);
      const data = await projectionsApi.list();
      setProjections(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadProjections();
  }, []);

  const handleCalculate = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    if (!form.accountId.trim() || !form.startDate || !form.endDate) {
      return;
    }

    const payload: LiquidityProjectionRequest = {
      accountId: form.accountId.trim(),
      startDate: form.startDate,
      endDate: form.endDate,
    };

    try {
      setCalculating(true);
      await toastPromise(projectionsApi.calculate(payload), {
        loading: 'Calculando proyecciones...',
        successFallback: 'Proyeccion calculada correctamente.',
        errorFallback: 'No fue posible calcular la proyeccion.',
      });
      await loadProjections();
      setForm(initialForm);
      setShowForm(false);
    } catch {
      // error handled by toast
    } finally {
      setCalculating(false);
    }
  };

  const projectionsByDate = useMemo(() => {
    const sorted = [...projections].sort(
      (a, b) => new Date(b.projectionDate).getTime() - new Date(a.projectionDate).getTime(),
    );
    const groups: Record<string, LiquidityProjection[]> = {};
    for (const p of sorted) {
      const key = p.projectionDate.slice(0, 7);
      if (!groups[key]) {
        groups[key] = [];
      }
      groups[key].push(p);
    }
    return groups;
  }, [projections]);

  const latestProjection = useMemo(() => {
    if (projections.length === 0) return null;
    return [...projections].sort(
      (a, b) => new Date(b.projectionDate).getTime() - new Date(a.projectionDate).getTime(),
    )[0];
  }, [projections]);

  const projectedBalance = latestProjection?.projectedBalance ?? 0;

  return (
    <section className={styles.wrapper}>
      <header className={styles.header}>
        <div>
          <h2 className={styles.title}>Proyecciones de Liquidez</h2>
          <p className={styles.subtitle}>
            Proyecta el saldo de tus cuentas en el tiempo basandote en tus transacciones recurrentes.
          </p>
        </div>
        <button
          type="button"
          className={styles.primaryButton}
          onClick={() => setShowForm(true)}
        >
          Nueva Proyeccion
        </button>
      </header>

      <div className={styles.summaryGrid}>
        <article className={styles.summaryCard}>
          <p className={styles.summaryLabel}>Proyecciones Totales</p>
          <strong className={styles.summaryValue}>{projections.length}</strong>
        </article>
        <article className={styles.summaryCard}>
          <p className={styles.summaryLabel}>Saldo Proyectado</p>
          <strong className={`${styles.summaryValue} ${styles.summaryPositive}`}>
            {formatCurrency(projectedBalance)}
          </strong>
        </article>
        <article className={styles.summaryCard}>
          <p className={styles.summaryLabel}>Ultima Proyeccion</p>
          <strong className={styles.summaryValue}>
            {latestProjection ? formatDate(latestProjection.projectionDate) : '---'}
          </strong>
        </article>
      </div>

      {loading ? <ProjectionsSkeleton /> : null}

      {!loading && projections.length === 0 ? (
        <div className={styles.emptyState}>
          <p>Aun no hay proyecciones de liquidez.</p>
          <button
            type="button"
            className={styles.primaryButton}
            onClick={() => setShowForm(true)}
          >
            Calcular Primera Proyeccion
          </button>
        </div>
      ) : null}

      {!loading && projections.length > 0 ? (
        <div className={styles.tableCard}>
          <div className={styles.tableHeader}>
            <h3>Todas las Proyecciones</h3>
          </div>
          <div className={styles.tableWrap}>
            <table>
              <thead>
                <tr>
                  <th>Fecha</th>
                  <th>Cuenta</th>
                  <th>Saldo Proyectado</th>
                </tr>
              </thead>
              <tbody>
                {Object.entries(projectionsByDate).map(([month, items]) => (
                  <tr key={month} className={styles.monthRow}>
                    <td colSpan={3}>
                      <strong>{month}</strong> ({items.length} proyecciones)
                    </td>
                  </tr>
                ))}
                {[...projections]
                  .sort(
                    (a, b) =>
                      new Date(b.projectionDate).getTime() - new Date(a.projectionDate).getTime(),
                  )
                  .slice(0, 50)
                  .map((p) => (
                    <tr key={p.id}>
                      <td>{formatDate(p.projectionDate)}</td>
                      <td>{p.account?.name ?? '---'}</td>
                      <td className={styles.amountCell}>
                        {formatCurrency(p.projectedBalance)}
                      </td>
                    </tr>
                  ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : null}

      {showForm ? (
        <div
          className={styles.overlay}
          role="presentation"
          onClick={() => setShowForm(false)}
        >
          <section
            className={styles.modal}
            role="dialog"
            aria-modal="true"
            onClick={(e) => e.stopPropagation()}
          >
            <header className={styles.modalHeader}>
              <h3>Calcular Proyeccion</h3>
              <button
                type="button"
                className={styles.closeButton}
                onClick={() => setShowForm(false)}
                aria-label="Cerrar"
              >
                x
              </button>
            </header>

            <form onSubmit={handleCalculate}>
              <div className={styles.modalBody}>
                <label className={styles.field}>
                  <span className={styles.label}>ID de la Cuenta</span>
                  <input
                    className={styles.input}
                    name="accountId"
                    value={form.accountId}
                    onChange={(e) =>
                      setForm((prev) => ({ ...prev, accountId: e.target.value }))
                    }
                    placeholder="UUID de la cuenta"
                    required
                  />
                </label>

                <label className={styles.field}>
                  <span className={styles.label}>Fecha Inicio</span>
                  <input
                    className={styles.input}
                    type="date"
                    name="startDate"
                    value={form.startDate}
                    onChange={(e) =>
                      setForm((prev) => ({ ...prev, startDate: e.target.value }))
                    }
                    required
                  />
                </label>

                <label className={styles.field}>
                  <span className={styles.label}>Fecha Fin</span>
                  <input
                    className={styles.input}
                    type="date"
                    name="endDate"
                    value={form.endDate}
                    onChange={(e) =>
                      setForm((prev) => ({ ...prev, endDate: e.target.value }))
                    }
                    required
                  />
                </label>
              </div>

              <footer className={styles.modalFooter}>
                <button
                  type="button"
                  className={styles.ghostButton}
                  onClick={() => setShowForm(false)}
                  disabled={calculating}
                >
                  Cancelar
                </button>
                <button
                  type="submit"
                  className={styles.primaryButton}
                  disabled={calculating}
                >
                  {calculating ? 'Calculando...' : 'Calcular'}
                </button>
              </footer>
            </form>
          </section>
        </div>
      ) : null}
    </section>
  );
}
