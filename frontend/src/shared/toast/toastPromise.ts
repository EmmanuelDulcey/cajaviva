import { toast, type ToastOptions } from 'react-hot-toast';
import { getBackendErrorMessage, getBackendMessage } from '../http/backendMessage';

type PromiseToastMessages<T> = {
  loading: string;
  successFallback: string;
  errorFallback: string;
  success?: (data: T) => string;
  error?: (error: unknown) => string;
};

export const toastPromise = async <T>(
  promise: Promise<T>,
  messages: PromiseToastMessages<T>,
  options?: ToastOptions,
): Promise<T> => {
  return toast.promise(
    promise,
    {
      loading: messages.loading,
      success: (data) => {
        if (messages.success) {
          return messages.success(data);
        }

        return getBackendMessage(data) ?? messages.successFallback;
      },
      error: (error) => {
        if (messages.error) {
          return messages.error(error);
        }

        return getBackendErrorMessage(error, messages.errorFallback);
      },
    },
    {
      style: {
        minWidth: '300px',
      },
      ...options,
    },
  );
};

