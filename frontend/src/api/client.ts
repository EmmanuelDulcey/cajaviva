import axios from "axios";

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/v1",
  withCredentials: true,
  withXSRFToken: true,
  xsrfCookieName: "XSRF-TOKEN",
  xsrfHeaderName: "X-XSRF-TOKEN",
});

let csrfBootstrapPromise: Promise<void> | null = null;

const readCookie = (name: string): string | null => {
  const cookiePrefix = `${name}=`;
  const cookies = document.cookie.split(";");

  for (const cookie of cookies) {
    const trimmedCookie = cookie.trim();
    if (trimmedCookie.startsWith(cookiePrefix)) {
      return decodeURIComponent(trimmedCookie.slice(cookiePrefix.length));
    }
  }

  return null;
};

const ensureCsrfToken = async (): Promise<void> => {
  if (readCookie("XSRF-TOKEN")) {
    return;
  }

  if (!csrfBootstrapPromise) {
    csrfBootstrapPromise = client
      .get("/auth/csrf")
      .then(() => undefined)
      .finally(() => {
        csrfBootstrapPromise = null;
      });
  }

  await csrfBootstrapPromise;
};

client.interceptors.request.use((config) => {
  return Promise.resolve(config).then(async (currentConfig) => {
    const method = currentConfig.method?.toUpperCase();
    const shouldAttachCsrf =
      method === "POST" || method === "PUT" || method === "PATCH" || method === "DELETE";
    const isCsrfBootstrapRequest = currentConfig.url?.includes("/auth/csrf");

    if (shouldAttachCsrf && !isCsrfBootstrapRequest) {
      await ensureCsrfToken();
      const csrfToken = readCookie("XSRF-TOKEN");
      if (csrfToken) {
        currentConfig.headers.set("X-XSRF-TOKEN", csrfToken);
      }
    }

    return currentConfig;
  });
});

export default client;
