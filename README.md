# CajaViva

CajaViva es una API backend desarrollada en Spring Boot para la gestión de información financiera de usuarios. El proyecto modela usuarios, cuentas, categorías, transacciones financieras, movimientos recurrentes, proyecciones de liquidez y alertas.

---

## Tecnologías y dependencias principales

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA (para entidades principales)
- JDBC puro/DAO manual (solo entidad User)
- SQL Server + H2 (dev/test)
- Flyway (migraciones)
- Springdoc OpenAPI 2.5 (Swagger)
- JWT + Refresh Tokens (autenticación con cookies)
- Spring Security, BCrypt

---

## Arquitectura del proyecto

```
src/main/java/com/cajaviva/cajaviva/
├── auth/            — Autenticación JWT con cookies (login, refresh, logout)
│   ├── config/      — Propiedades de JWT y cookies
│   ├── controller/  — AuthController
│   ├── dto/         — LoginRequest, AuthResponse
│   ├── security/    — Filtros JWT, manejadores 401/403, UserPrincipal
│   └── service/     — AuthService, JwtService, CookieService, RefreshTokenService
├── config/          — SecurityConfig, OpenApiConfig
├── controller/      — Endpoints REST (por entidad) + DTOs específicos
├── dao/             — Contrato DAO JDBC para User
│   └── impl/        — Implementación JDBC manual para User
├── dto/             — DTOs compartidos (RegisterUserRequest, etc.)
├── entity/          — Modelos de dominio (JPA entities + POJO User)
├── exception/       — Manejo centralizado de errores (GlobalExceptionHandler)
├── repository/JPA/  — Interfaces JpaRepository
├── service/         — Interfaces de servicio
│   └── impl/        — Implementaciones con JPA y JDBC
└── utilities/       — Clases utilitarias
```

---

## Estado de persistencia por entidad

| Entidad                  | Persistencia    | Paquetes principales         |
|--------------------------|-----------------|------------------------------|
| User                     | JDBC/DAO        | dao/, dao/impl/, service/    |
| Account                  | JPA             | repository/JPA/, service/    |
| Category                 | JPA             | repository/JPA/, service/    |
| Alert                    | JPA             | repository/JPA/, service/    |
| FinancialTransaction     | JPA             | repository/JPA/, service/    |
| LiquidityProjection      | JPA             | repository/JPA/, service/    |
| RecurrentTransaction     | JPA             | repository/JPA/, service/    |
| UserAccess               | JPA             | repository/JPA/, service/    |
| AuthUser                 | JPA             | repository/JPA/, service/    |
| RefreshToken             | JPA             | repository/JPA/, service/    |

> Solo la entidad User mantiene acceso directo con JDBC. Todas las demás entidades están gestionadas por Spring Data JPA.

---

## Requisitos previos

- Java 17+
- Maven (o `mvnw.cmd`/`mvnw`)
- SQL Server accesible (o perfil H2 para desarrollo rápido)

---

## Configuración de base de datos

Dos perfiles disponibles:

| Perfil       | Base de datos | Flyway | Uso                     |
|-------------|---------------|--------|--------------------------|
| `sqlserver` | SQL Server    | Sí     | Producción/desarrollo    |
| `h2`        | H2 en memoria | No     | Tests rápidos            |

Activar con:
```sh
mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
# o
$env:SPRING_PROFILES_ACTIVE="sqlserver"
```

### Variables de entorno

| Variable          | Descripción                          | Valor por defecto                                                   |
|-------------------|--------------------------------------|----------------------------------------------------------------------|
| `DB_URL`          | URL de conexión JDBC                 | `jdbc:sqlserver://localhost:1433;databaseName=CajaViva;...`          |
| `DB_USER`         | Usuario de base de datos             | `sa`                                                                |
| `DB_PASSWORD`     | Contraseña de base de datos          | `admin`                                                              |
| `JWT_SIGNING_KEY` | Clave secreta para firmar JWT        | `01234567890123456789012345678901`                                  |

---

## Inicio rápido

```sh
git clone https://github.com/EmmanuelDulcey/cajaviva.git
cd cajaviva

# Crear base de datos CajaViva en SQL Server

# Configurar credenciales
$env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=CajaViva;encrypt=true;trustServerCertificate=true"
$env:DB_USER="sa"
$env:DB_PASSWORD="tu_password"

# Ejecutar
./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=sqlserver
```

---

## Endpoints de la API

**Base:** `http://localhost:8080/v1`  
**Swagger UI:** `http://localhost:8080/v1/swagger-ui.html`  
**OpenAPI:** `http://localhost:8080/v1/api-docs`

### Autenticación

| Método | Endpoint              | Descripción                    |
|--------|-----------------------|--------------------------------|
| POST   | `/auth/login`         | Iniciar sesión                 |
| POST   | `/auth/refresh`       | Refrescar token de acceso      |
| POST   | `/auth/logout`        | Cerrar sesión                  |
| GET    | `/auth/me`            | Obtener sesión actual          |
| GET    | `/auth/csrf`          | Obtener token CSRF             |

### Registro público

| Método | Endpoint          | Descripción              |
|--------|-------------------|--------------------------|
| POST   | `/registries/users` | Registrar nuevo usuario |

### User (JDBC)

| Método | Endpoint        | Descripción             |
|--------|-----------------|-------------------------|
| GET    | `/users`        | Listar usuarios         |
| GET    | `/users/{id}`   | Obtener usuario por ID  |
| POST   | `/users`        | Crear usuario           |
| PUT    | `/users/{id}`   | Actualizar usuario      |
| DELETE | `/users/{id}`   | Eliminar usuario        |

### Account

| Método | Endpoint                     | Descripción                          |
|--------|------------------------------|--------------------------------------|
| GET    | `/accounts`                  | Listar cuentas                       |
| GET    | `/accounts/user/{user_id}`   | Listar cuentas por usuario           |
| GET    | `/accounts/{id}`             | Obtener cuenta con proyección 30 días |
| POST   | `/accounts`                  | Crear cuenta                         |
| PUT    | `/accounts/{id}`             | Actualizar cuenta                    |
| DELETE | `/accounts/{id}`             | Eliminar cuenta                      |

### Category

| Método | Endpoint               | Descripción            |
|--------|------------------------|------------------------|
| GET    | `/api/categories`      | Listar categorías      |
| GET    | `/api/categories/{id}` | Obtener categoría      |
| POST   | `/api/categories`      | Crear categoría        |
| PUT    | `/api/categories/{id}` | Actualizar categoría   |
| DELETE | `/api/categories/{id}` | Eliminar categoría     |

### FinancialTransaction

| Método | Endpoint                              | Descripción                     |
|--------|---------------------------------------|----------------------------------|
| GET    | `/api/transactions`                   | Listar transacciones             |
| GET    | `/api/transactions/{id}`              | Obtener transacción              |
| GET    | `/api/transactions/account/{account_id}` | Listar por cuenta              |
| POST   | `/api/transactions`                   | Crear transacción                |
| PUT    | `/api/transactions/{id}`              | Actualizar transacción           |
| DELETE | `/api/transactions/{id}`              | Eliminar transacción             |

### LiquidityProjection

| Método | Endpoint                                      | Descripción                          |
|--------|-----------------------------------------------|--------------------------------------|
| GET    | `/api/liquidity-projections`                  | Listar proyecciones                  |
| GET    | `/api/liquidity-projections/{id}`             | Obtener proyección por ID            |
| GET    | `/api/liquidity-projections/account/{account_id}` | Listar por cuenta                 |
| POST   | `/api/liquidity-projections`                  | Crear proyección manual              |
| POST   | `/api/liquidity-projections/calculate`        | Calcular proyección en rango fechas  |
| PUT    | `/api/liquidity-projections/{id}`             | Actualizar proyección                |
| DELETE | `/api/liquidity-projections/{id}`             | Eliminar proyección                  |

### RecurrentTransaction

| Método | Endpoint                                      | Descripción                     |
|--------|-----------------------------------------------|----------------------------------|
| GET    | `/api/recurrent-transactions`                 | Listar transacciones recurrentes |
| GET    | `/api/recurrent-transactions/{id}`            | Obtener por ID                   |
| GET    | `/api/recurrent-transactions/account/{account_id}` | Listar por cuenta          |
| POST   | `/api/recurrent-transactions`                 | Crear                            |
| PUT    | `/api/recurrent-transactions/{id}`            | Actualizar                       |
| DELETE | `/api/recurrent-transactions/{id}`            | Eliminar                         |

### Alert

| Método | Endpoint                                      | Descripción            |
|--------|-----------------------------------------------|------------------------|
| GET    | `/api/alerts`                                 | Listar alertas         |
| GET    | `/api/alerts/{id}`                            | Obtener alerta         |
| GET    | `/api/alerts/projection/{projection_id}`      | Listar por proyección  |
| POST   | `/api/alerts`                                 | Crear alerta           |
| PUT    | `/api/alerts/{id}`                            | Actualizar alerta      |
| DELETE | `/api/alerts/{id}`                            | Eliminar alerta        |

### UserAccess

| Método | Endpoint                                         | Descripción          |
|--------|--------------------------------------------------|----------------------|
| GET    | `/api/user-accesses`                             | Listar accesos       |
| GET    | `/api/user-accesses/user/{user_id}`              | Listar por usuario   |
| GET    | `/api/user-accesses/account/{account_id}`        | Listar por cuenta    |
| GET    | `/api/user-accesses/{id}`                        | Obtener acceso       |
| POST   | `/api/user-accesses`                             | Crear acceso         |
| PUT    | `/api/user-accesses/{id}`                        | Actualizar acceso    |
| DELETE | `/api/user-accesses/{id}`                        | Eliminar acceso      |

---

## Manejo de errores

`GlobalExceptionHandler` centraliza excepciones y retorna respuestas JSON:

| Código | Descripción                          |
|--------|--------------------------------------|
| 200    | Éxito                                |
| 201    | Recurso creado                       |
| 400    | Error de validación o parámetro      |
| 401    | No autenticado                       |
| 403    | Acceso denegado                      |
| 404    | Recurso no encontrado                |
| 409    | Conflicto (duplicado)                |
| 500    | Error interno                        |

```json
{
  "message": "Descripción del error",
  "code": "CODIGO_DE_ERROR",
  "timestamp": "2026-05-05T12:00:00"
}
```

---

## Migraciones (Flyway)

Los scripts están en `src/main/resources/db/migration/`:
- `V1__init.sql` — Esquema inicial (8 tablas)
- `V2__auth_refresh_tokens.sql` — Tabla de refresh tokens
- `V3__align_jpa_with_schema.sql` — Ajustes de columnas

---

## Pruebas

```sh
# Todas las pruebas
mvn clean test

# Prueba específica
mvn -Dtest=LiquidityProjectionServiceTest test
```

El proyecto incluye **tests unitarios** con JUnit 5 y Mockito para:
- Servicios (cobertura de lógica de negocio)
- Controladores (seguridad, acceso autenticado)
- DAOs (JDBC)

---

## Frontend

El frontend React está en el directorio `frontend/`. Ver su [README](frontend/README.md).

```sh
cd frontend
npm install
npm run dev
```

---

## Créditos

Desarrollado por Emmanuel Dulcey  
[Repositorio en GitHub](https://github.com/EmmanuelDulcey/cajaviva)
