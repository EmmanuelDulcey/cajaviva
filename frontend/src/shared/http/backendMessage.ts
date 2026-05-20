import axios from 'axios';

type BackendPayload = {
  message?: unknown;
};

export const getBackendMessage = (payload: unknown): string | null => {
  if (!payload || typeof payload !== 'object') {
    return null;
  }

  const message = (payload as BackendPayload).message;
  return typeof message === 'string' && message.trim().length > 0 ? message : null;
};

export const getBackendErrorMessage = (error: unknown, fallback: string): string => {
  if (axios.isAxiosError(error)) {
    const responseMessage = getBackendMessage(error.response?.data);
    if (responseMessage) {
      return responseMessage;
    }
  }

  return fallback;
};

