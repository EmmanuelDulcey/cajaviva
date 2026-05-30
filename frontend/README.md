# CajaViva Frontend

Frontend de la aplicación CajaViva, desarrollado con React 19, TypeScript 6 y Vite 8.

---

## Tecnologías

- **React** 19.x
- **TypeScript** 6.x
- **Vite** 8.x
- **Axios** para comunicación con la API
- **react-hot-toast** para notificaciones
- **Cypress** para pruebas E2E

---

## Requisitos previos

- Node.js 18+
- npm

---

## Instalación

```sh
npm install
```

---

## Configuración

1. Copia el archivo de entorno:
   ```sh
   cp .env.example .env
   ```

2. Edita `.env`:
   ```
   VITE_API_BASE_URL=http://localhost:8080/v1
   ```

---

## Scripts

| Comando            | Descripción                         |
|--------------------|-------------------------------------|
| `npm run dev`      | Servidor de desarrollo (Vite)       |
| `npm run build`    | Compilación para producción         |
| `npm run lint`     | Ejecutar ESLint                     |
| `npm run preview`  | Vista previa de producción          |

---

## Estructura del proyecto

```
src/
├── api/
│   └── client.ts                — Cliente Axios con CSRF
├── assets/                      — SVG, PNG (branding, dashboard, sidebar)
├── features/
│   ├── auth/                    — Login, registro, sesión
│   │   ├── api/                 — authApi.ts, registerUser.ts
│   │   ├── components/          — LoginForm, RegisterForm
│   │   ├── pages/               — LoginPage, RegisterPage, AuthenticatedHomePage
│   │   ├── session/             — AuthSessionContext, authSessionService
│   │   └── types/               — login.ts, register.ts
│   ├── categories/              — CRUD de categorías
│   │   ├── api/                 — categoriesApi.ts
│   │   ├── pages/               — CategoriesPage
│   │   └── types/               — category.ts
│   └── projections/             — Proyecciones de liquidez
│       ├── api/                 — projectionsApi.ts
│       ├── pages/               — ProjectionsPage
│       └── types/               — projection.ts
├── shared/
│   ├── components/skeleton/     — Componente Skeleton (carga)
│   ├── http/                    — backendMessage.ts
│   └── toast/                   — AppToaster, toastPromise
├── styles/                      — global.css, tokens.css
├── App.tsx                      — Enrutador principal
└── main.tsx                     — Punto de entrada
```

---

## Pruebas E2E (Cypress)

Los tests E2E están en `cypress/e2e/`:

| Archivo                | Descripción                     |
|------------------------|---------------------------------|
| `login.cy.ts`          | Flujo de inicio de sesión       |
| `transactions.cy.ts`   | Listar, crear, editar, eliminar |

Ejecutar:
```sh
npx cypress run
# o modo interactivo:
npx cypress open
```

---

## Variables de entorno

| Variable              | Descripción                | Valor por defecto                 |
|-----------------------|----------------------------|-----------------------------------|
| `VITE_API_BASE_URL`   | URL base de la API backend | `http://localhost:8080/v1`        |

---

## Features modulares

Cada feature sigue una estructura consistente:

```
features/<nombre>/
├── api/        — Llamadas HTTP (Axios)
├── components/ — Componentes reutilizables
├── pages/      — Páginas principales
├── session/    — Contexto/estado (opcional)
└── types/      — Interfaces TypeScript
```

Los estilos se implementan con **CSS Modules** (`.module.css`) para encapsulación.
