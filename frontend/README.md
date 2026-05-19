# CajaViva Frontend

Frontend de la aplicación CajaViva, desarrollado con React, TypeScript y Vite.

---

## Tecnologías

- **React** 19.x
- **TypeScript** 6.x
- **Vite** 8.x
- **Axios** para comunicación con la API

---

## Requisitos previos

- Node.js 18+ 
- npm o pnpm

---

## Instalación

```sh
npm install
```

---

## Configuración

1. Copia el archivo de entorno de ejemplo:
   ```sh
   cp .env.example .env
   ```

2. Edita `.env` y establece la URL base de la API:
   ```
   VITE_API_BASE_URL=http://localhost:8080/v1
   ```

---

## Scripts disponibles

| Comando        | Descripción                        |
|----------------|-------------------------------------|
| `npm run dev`  | Inicia el servidor de desarrollo   |
| `npm run build` | Compila el proyecto para producción |
| `npm run lint` | Ejecuta ESLint                      |
| `npm run preview` | Vista previa de producción       |

---

## Estructura del proyecto

- `src/api/client.ts` — Cliente Axios configurado para la API
- `src/` — Componentes React y lógica de la aplicación

---

## Variables de entorno

| Variable              | Descripción                          | Valor por defecto             |
|-----------------------|--------------------------------------|-------------------------------|
| `VITE_API_BASE_URL`   | URL base de la API backend           | `http://localhost:8080/v1`    |