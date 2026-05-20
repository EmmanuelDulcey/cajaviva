import { Toaster, ToastBar, toast } from 'react-hot-toast';
import styles from './AppToaster.module.css';

export function AppToaster() {
  return (
    <Toaster
      position="top-right"
      toastOptions={{
        duration: 4500,
        style: {
          background: '#ffffff',
          color: '#1a1c1e',
          border: '1px solid #d7dfdb',
          borderRadius: '16px',
        },
      }}
    >
      {(t) => (
        <ToastBar toast={t}>
          {({ icon, message }) => (
            <div className={styles.content}>
              <span className={styles.message}>
                {icon}
                {message}
              </span>
              <button
                type="button"
                className={styles.closeButton}
                aria-label="Cerrar notificacion"
                onClick={() => toast.dismiss(t.id)}
              >
                ×
              </button>
            </div>
          )}
        </ToastBar>
      )}
    </Toaster>
  );
}

