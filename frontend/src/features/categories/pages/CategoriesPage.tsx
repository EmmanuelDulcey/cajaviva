import { useEffect, useMemo, useRef, useState, type ChangeEvent, type FormEvent } from 'react';
import walletIcon from '../../../assets/dashboard/wallet-icon.svg';
import { Skeleton } from '../../../shared/components/skeleton/Skeleton';
import { toastPromise } from '../../../shared/toast/toastPromise';
import { categoriesApi } from '../api/categoriesApi';
import type { Category, CategoryUpsertPayload } from '../types/category';
import styles from './CategoriesPage.module.css';

type CategoryFilter = 'all' | 'income' | 'expense';

type CategoryForm = {
  name: string;
  type: number;
  description: string;
};

type CategoryFormErrors = {
  name?: string;
  description?: string;
};

const initialForm: CategoryForm = {
  name: '',
  type: 1,
  description: '',
};

const validateForm = (form: CategoryForm): CategoryFormErrors => {
  const errors: CategoryFormErrors = {};

  if (!form.name.trim()) {
    errors.name = 'El nombre es obligatorio.';
  }

  if (form.description.length > 500) {
    errors.description = 'La descripcion no puede superar 500 caracteres.';
  }

  return errors;
};

function CategoryCardsSkeleton() {
  return (
    <section className={styles.grid} aria-label="Cargando categorias">
      {Array.from({ length: 6 }).map((_, index) => (
        <article key={index} className={styles.card}>
          <div className={styles.cardTop}>
            <Skeleton className={styles.skeletonIcon} />
            <Skeleton className={styles.skeletonChip} />
          </div>
          <Skeleton className={styles.skeletonTitle} />
          <Skeleton className={styles.skeletonText} />
          <Skeleton className={styles.skeletonTextShort} />
          <div className={styles.cardDivider}>
            <Skeleton className={styles.skeletonEdit} />
          </div>
        </article>
      ))}
    </section>
  );
}

export function CategoriesPage() {
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState<Category[]>([]);
  const [activeFilter, setActiveFilter] = useState<CategoryFilter>('all');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isCreateMode, setIsCreateMode] = useState(false);
  const [selectedCategoryId, setSelectedCategoryId] = useState<string | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState<CategoryForm>(initialForm);
  const [touched, setTouched] = useState<Record<keyof CategoryForm, boolean>>({
    name: false,
    type: false,
    description: false,
  });

  const nameInputRef = useRef<HTMLInputElement | null>(null);
  const descriptionInputRef = useRef<HTMLTextAreaElement | null>(null);

  const errors = useMemo(() => validateForm(form), [form]);

  const loadCategories = async () => {
    try {
      setLoading(true);
      const data = await categoriesApi.list();
      setCategories(data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void loadCategories();
  }, []);

  const filteredCategories = useMemo(() => {
    if (activeFilter === 'income') {
      return categories.filter((category) => category.type === 2);
    }

    if (activeFilter === 'expense') {
      return categories.filter((category) => category.type === 1);
    }

    return categories;
  }, [activeFilter, categories]);

  const resetModalState = () => {
    setForm(initialForm);
    setTouched({
      name: false,
      type: false,
      description: false,
    });
    setSelectedCategoryId(null);
    setIsCreateMode(false);
    setIsModalOpen(false);
  };

  const openCreateModal = () => {
    setForm(initialForm);
    setTouched({
      name: false,
      type: false,
      description: false,
    });
    setSelectedCategoryId(null);
    setIsCreateMode(true);
    setIsModalOpen(true);
  };

  const openEditModal = async (id: string) => {
    try {
      const category = await categoriesApi.show(id);

      setForm({
        name: category.name ?? '',
        type: category.type,
        description: category.description ?? '',
      });
      setTouched({
        name: false,
        type: false,
        description: false,
      });
      setSelectedCategoryId(id);
      setIsCreateMode(false);
      setIsModalOpen(true);
    } catch {
      // keep silent for show request
    }
  };

  const handleInputChange = (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
    setTouched((current) => ({ ...current, [name]: true }));
  };

  const handleInputBlur = (event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name } = event.target;
    setTouched((current) => ({ ...current, [name]: true }));
  };

  const handleTypeChange = (type: number) => {
    setForm((current) => ({ ...current, type }));
    setTouched((current) => ({ ...current, type: true }));
  };

  const focusFirstError = () => {
    if (errors.name) {
      nameInputRef.current?.focus();
      return;
    }

    if (errors.description) {
      descriptionInputRef.current?.focus();
    }
  };

  const handleSave = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setTouched({ name: true, type: true, description: true });

    if (Object.keys(errors).length > 0) {
      focusFirstError();
      return;
    }

    const payload: CategoryUpsertPayload = {
      name: form.name.trim(),
      type: form.type,
      description: form.description.trim(),
    };

    try {
      setSubmitting(true);

      if (isCreateMode) {
        await toastPromise(categoriesApi.create(payload), {
          loading: 'Creando categoria...',
          successFallback: 'Categoria creada correctamente.',
          errorFallback: 'No fue posible crear la categoria.',
        });
      } else if (selectedCategoryId) {
        await toastPromise(categoriesApi.update(selectedCategoryId, payload), {
          loading: 'Guardando cambios...',
          successFallback: 'Categoria actualizada correctamente.',
          errorFallback: 'No fue posible actualizar la categoria.',
        });
      }

      await loadCategories();
      resetModalState();
    } catch {
      // keep silent for show request
    } finally {
      setSubmitting(false);
    }
  };

  const handleDelete = async () => {
    if (!selectedCategoryId || isCreateMode) {
      return;
    }

    try {
      setSubmitting(true);
      await toastPromise(categoriesApi.remove(selectedCategoryId), {
        loading: 'Eliminando categoria...',
        successFallback: 'Categoria eliminada correctamente.',
        errorFallback: 'No fue posible eliminar la categoria.',
      });
      await loadCategories();
      resetModalState();
    } catch {
      // keep silent for show request
    } finally {
      setSubmitting(false);
    }
  };

  const emptyState = !loading && categories.length === 0;

  return (
    <section className={styles.wrapper}>
      <header>
        <h2 className={styles.title}>Categorias</h2>
        <p className={styles.subtitle}>
          Gestiona y organiza tus tipos de transacciones para una mejor claridad financiera.
        </p>
      </header>

      <div className={styles.filters}>
        <button
          type="button"
          className={`${styles.chip} ${activeFilter === 'all' ? styles.chipActive : ''}`}
          onClick={() => setActiveFilter('all')}
        >
          Todas
        </button>
        <button
          type="button"
          className={`${styles.chip} ${activeFilter === 'income' ? styles.chipActive : ''}`}
          onClick={() => setActiveFilter('income')}
        >
          Ingresos
        </button>
        <button
          type="button"
          className={`${styles.chip} ${activeFilter === 'expense' ? styles.chipActive : ''}`}
          onClick={() => setActiveFilter('expense')}
        >
          Gastos
        </button>
      </div>

      {loading ? <CategoryCardsSkeleton /> : null}

      {!loading ? (
        <section className={styles.grid}>
          {!emptyState
            ? filteredCategories.map((category) => (
                <article key={category.id} className={styles.card}>
                  <div className={styles.cardTop}>
                    <span className={styles.iconWrap}>
                      <img src={walletIcon} alt="" aria-hidden="true" />
                    </span>
                    <span
                      className={`${styles.typeBadge} ${
                        category.type === 2 ? styles.typeIncome : styles.typeExpense
                      }`}
                    >
                      {category.type === 2 ? 'Ingreso' : 'Gasto'}
                    </span>
                  </div>
                  <h3 className={styles.cardTitle}>{category.name}</h3>
                  <p className={styles.cardDescription}>{category.description ?? ''}</p>
                  <div className={styles.cardDivider}>
                    <button
                      type="button"
                      className={styles.editButton}
                      onClick={() => void openEditModal(category.id)}
                      aria-label={`Editar categoria ${category.name}`}
                    >
                      Editar
                    </button>
                  </div>
                </article>
              ))
            : null}

          <button type="button" className={styles.createCard} onClick={openCreateModal}>
            <div className={styles.createContent}>
              <span className={styles.createIcon}>+</span>
              <p className={styles.createText}>Crear Nueva Categoria</p>
            </div>
          </button>
        </section>
      ) : null}

      {isModalOpen ? (
        <div className={styles.overlay} role="presentation" onClick={resetModalState}>
          <section className={styles.modal} role="dialog" aria-modal="true" onClick={(e) => e.stopPropagation()}>
            <header className={styles.modalHeader}>
              <h3>{isCreateMode ? 'Crear Categoria' : 'Editar Categoria'}</h3>
              <button type="button" className={styles.closeButton} onClick={resetModalState} aria-label="Cerrar modal">
                x
              </button>
            </header>

            <form onSubmit={handleSave} noValidate>
              <div className={styles.modalBody}>
                <label className={styles.field}>
                  <span className={styles.label}>Nombre</span>
                  <input
                    ref={nameInputRef}
                    className={`${styles.input} ${touched.name && errors.name ? styles.inputError : ''}`}
                    name="name"
                    value={form.name}
                    onChange={handleInputChange}
                    onBlur={handleInputBlur}
                    required
                  />
                  {touched.name && errors.name ? <span className={styles.fieldError}>{errors.name}</span> : null}
                </label>

                <div className={styles.field}>
                  <span className={styles.label}>Tipo</span>
                  <div className={styles.typeToggle}>
                    <button
                      type="button"
                      className={`${styles.typeButton} ${form.type === 2 ? styles.typeButtonActive : ''}`}
                      onClick={() => handleTypeChange(2)}
                    >
                      Ingreso
                    </button>
                    <button
                      type="button"
                      className={`${styles.typeButton} ${form.type === 1 ? styles.typeButtonActive : ''}`}
                      onClick={() => handleTypeChange(1)}
                    >
                      Gasto
                    </button>
                  </div>
                </div>

                <label className={styles.field}>
                  <span className={styles.label}>Descripcion</span>
                  <textarea
                    ref={descriptionInputRef}
                    className={`${styles.textarea} ${
                      touched.description && errors.description ? styles.inputError : ''
                    }`}
                    name="description"
                    value={form.description}
                    onChange={handleInputChange}
                    onBlur={handleInputBlur}
                  />
                  {touched.description && errors.description ? (
                    <span className={styles.fieldError}>{errors.description}</span>
                  ) : null}
                </label>
              </div>

              <footer className={styles.modalFooter}>
                {!isCreateMode ? (
                  <button type="button" className={styles.dangerButton} disabled={submitting} onClick={() => void handleDelete()}>
                    Eliminar
                  </button>
                ) : null}
                <div className={styles.footerActions}>
                  <button type="button" className={styles.ghostButton} onClick={resetModalState} disabled={submitting}>
                    Cancelar
                  </button>
                  <button type="submit" className={styles.primaryButton} disabled={submitting}>
                    {isCreateMode ? 'Guardar' : 'Guardar Cambios'}
                  </button>
                </div>
              </footer>
            </form>
          </section>
        </div>
      ) : null}
    </section>
  );
}

