# CajaViva

CajaViva es una API backend desarrollada en Spring Boot para la gestión de información financiera de usuarios. El proyecto modela usuarios, cuentas, categorías, transacciones financieras, movimientos recurrentes, proyecciones de liquidez y alertas.  
**Esta implementación ilustra la coexistencia de persistencia clásica JDBC (sólo para User) y Spring Data JPA (para todas las demás entidades).**

---

## Tecnologías y dependencias principales

- Java 17
- Spring Boot 3.x
- Spring Data JPA (para entidades principales)
- JDBC puro/DAO manual (solo entidad User)
- SQL Server
- Flyway (migraciones)
- Springdoc OpenAPI (documentación Swagger)

---

## Arquitectura del proyecto

- **controller/**: Endpoints REST (por entidad)
- **entity/**: Modelos de dominio (entidades/POJOs)
- **repository/JPA/**: Interfaces `JpaRepository` para persistencia JPA (solo entidades migradas)
- **dao/**: Contrato DAO JDBC para User
- **dao/impl/**: Implementación JDBC/DAO manual para User
- **service/**: Lógica de negocio por entidad; servicios JPA y JDBC
- **service/impl/**: Implementaciones de capa de servicio
- **config/**: Configuración de beans, JDBC y OpenAPI
- **exception/**: Manejo centralizado de errores
- **utilities/**: Clases utilitarias (ejemplo: conexión JDBC)
- **db/migration/**: Scripts Flyway para inicialización de base de datos

---

## Estado de persistencia por entidad

| Entidad                  | Persistencia    | Paquetes principales                           | Notas                                                |
|--------------------------|----------------|-----------------------------------------------|------------------------------------------------------|
| User                     | JDBC/DAO       | dao/, dao/impl/, service/, config/UserConfig   | JDBC puro por requisito, ejemplo de legado/transición |
| Account                  | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |
| Category                 | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |
| Alert                    | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |
| FinancialTransaction     | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |
| LiquidityProjection      | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |
| RecurrentTransaction     | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |
| UserAccess               | JPA            | repository/JPA/, service/                     | Migrada a JPA                                        |

> **Nota:**  
> Sólo la entidad User mantiene acceso directo con JDBC y estructura DAO manual. Todas las demás entidades están gestionadas exclusivamente por Spring Data JPA.

---

## Requisitos previos

- Java 17+
- Maven (o uso de `mvnw.cmd`/`mvnw`)
- SQL Server accesible (instancia local o cloud)
- SQL Server Management Studio (opcional, para administración y troubleshooting)

---

## Configuración de base de datos

Por defecto, este proyecto usa dos perfiles: **sqlserver** y **h2**.

- El perfil **sqlserver** utiliza tu instancia de SQL Server real — recomendado para desarrollo o pruebas serias, permite persistencia real de datos.
- El perfil **h2** utiliza una base de datos en memoria _temporal_, ideal para pruebas rápidas (los datos se borran en cada arranque y los CRUD parecerán "no funcionar").

La app lee las siguientes variables de entorno o valores por defecto:

- `DB_URL` (por defecto: `jdbc:sqlserver://localhost:1433;databaseName=CajaViva;encrypt=true;trustServerCertificate=true`)
- `DB_USER` (por defecto: `sa`)
- `DB_PASSWORD` (por defecto: `admin`)

Para cambiar la configuración, sobrescribe variables de entorno antes de iniciar la app.

**IMPORTANTE:** ¡Si quieres persistencia real y probar bien los CRUD (incluido User), debes activar el perfil sqlserver! Puedes hacerlo así:

- Con Maven (recomendado):
  ```sh
  mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
  ```
- O con variable de entorno:
  ```sh
  $env:SPRING_PROFILES_ACTIVE="sqlserver"
  ```

---

## Preparación e inicio

1. **Clona el repositorio:**
   ```sh
   git clone https://github.com/EmmanuelDulcey/cajaviva.git
   cd cajaviva
   ```

2. **Crea la base de datos**  
   Asegúrate de tener una base llamada `CajaViva` en tu instancia de SQL Server.

3. **Configura las credenciales de acceso**  
   Ejemplo en PowerShell:
   ```sh
   $env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=CajaViva;encrypt=true;trustServerCertificate=true"
   $env:DB_USER="sa"
   $env:DB_PASSWORD="tu_password"
   ```

4. **Lanza la aplicación** (en modo SQLSERVER para persistencia):
   - Con Maven:
     ```sh
     mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
     ```
   - O con wrapper:
     ```sh
     ./mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=sqlserver
     ```

---

## Endpoints principales

- **API base:**
  http://localhost:8080/v1
- **Swagger UI:**
  http://localhost:8080/v1/swagger-ui.html
- **OpenAPI:**
  http://localhost:8080/v1/api-docs

---

## Endpoints de la API

### User
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/users` | Listar todos los usuarios |
| GET | `/users/{id}` | Obtener usuario por ID |
| POST | `/users` | Crear usuario |
| PUT | `/users/{id}` | Actualizar usuario |
| DELETE | `/users/{id}` | Eliminar usuario |

### Account
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/accounts` | Listar todas las cuentas |
| GET | `/accounts/user/{user_id}` | Listar cuentas por usuario |
| GET | `/accounts/{id}` | Obtener cuenta por ID |
| POST | `/accounts` | Crear cuenta |
| PUT | `/accounts/{id}` | Actualizar cuenta |
| DELETE | `/accounts/{id}` | Eliminar cuenta |

### UserAccess
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/user-accesses` | Listar todos los accesos |
| GET | `/api/user-accesses/user/{user_id}` | Listar accesos por usuario |
| GET | `/api/user-accesses/account/{account_id}` | Listar accesos por cuenta |
| GET | `/api/user-accesses/{id}` | Obtener acceso por ID |
| POST | `/api/user-accesses` | Crear acceso |
| PUT | `/api/user-accesses/{id}` | Actualizar acceso |
| DELETE | `/api/user-accesses/{id}` | Eliminar acceso |

### Category
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/categories` | Listar todas las categorías |
| GET | `/api/categories/{id}` | Obtener categoría por ID |
| POST | `/api/categories` | Crear categoría |
| PUT | `/api/categories/{id}` | Actualizar categoría |
| DELETE | `/api/categories/{id}` | Eliminar categoría |

### FinancialTransaction
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/transactions` | Listar todas las transacciones |
| GET | `/api/transactions/{id}` | Obtener transacción por ID |
| GET | `/api/transactions/account/{account_id}` | Listar transacciones por cuenta |
| POST | `/api/transactions` | Crear transacción |
| PUT | `/api/transactions/{id}` | Actualizar transacción |
| DELETE | `/api/transactions/{id}` | Eliminar transacción |

### Alert
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/alerts` | Listar todas las alertas |
| GET | `/api/alerts/{id}` | Obtener alerta por ID |
| GET | `/api/alerts/projection/{projection_id}` | Listar alertas por proyección |
| POST | `/api/alerts` | Crear alerta |
| PUT | `/api/alerts/{id}` | Actualizar alerta |
| DELETE | `/api/alerts/{id}` | Eliminar alerta |

### LiquidityProjection
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/liquidity-projections` | Listar todas las proyecciones |
| GET | `/api/liquidity-projections/{id}` | Obtener proyección por ID |
| GET | `/api/liquidity-projections/account/{account_id}` | Listar proyecciones por cuenta |
| POST | `/api/liquidity-projections` | Crear proyección |
| PUT | `/api/liquidity-projections/{id}` | Actualizar proyección |
| DELETE | `/api/liquidity-projections/{id}` | Eliminar proyección |

### RecurrentTransaction
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/recurrent-transactions` | Listar todas las transacciones recurrentes |
| GET | `/api/recurrent-transactions/{id}` | Obtener transacción recurrente por ID |
| GET | `/api/recurrent-transactions/account/{account_id}` | Listar transacciones recurrentes por cuenta |
| POST | `/api/recurrent-transactions` | Crear transacción recurrente |
| PUT | `/api/recurrent-transactions/{id}` | Actualizar transacción recurrente |
| DELETE | `/api/recurrent-transactions/{id}` | Eliminar transacción recurrente |

---

## Manejo de errores

La API cuenta con un sistema centralizado de manejo de excepciones (`GlobalExceptionHandler`) que retorna respuestas JSON estructuradas:

| Código HTTP | Descripción |
|-------------|-------------|
| 200 | Éxito |
| 201 | Recurso creado |
| 400 | Error de validación o parámetro inválido |
| 404 | Recurso no encontrado |
| 409 | Conflicto (recurso duplicado) |
| 500 | Error interno del servidor |

**Formato de respuesta de error:**
```json
{
  "message": "Descripción del error",
  "code": "CODIGO_DE_ERROR",
  "timestamp": "2026-05-05T12:00:00"
}
```

---

## Migraciones automáticas (Flyway)

- Todas las tablas se crean automáticamente al inicio si la base está vacía.
- Los scripts de migración están en `/src/main/resources/db/migration`.

---

## Consideraciones y troubleshooting

- **TCP/IP**  
  Activa el protocolo TCP/IP en SQL Server para aceptar conexiones externas (guía en documentación oficial de Microsoft).
- **Modo de autenticación**  
  Activa "Mixed Mode" (SQL+Windows Authentication) para usar usuario/password.
- **Puerto y usuario**  
  Si usas otro puerto o usuario distinto, actualiza `DB_URL` y credenciales.
- **Primera ejecución**  
  Si tienes error de login (14060), asegúrate que el usuario no exista duplicado o crea uno nuevo exclusivo para la app.

---

## Notas sobre arquitectura

- Arquitectura en capas (Controller → Service → Repository/JdbcDao → BD).
- User mantiene JDBC manual para demostración y compatibilidad.
- Entidades migradas a JPA aprovechan JpaRepository y relaciones automáticas.
- Centralización de errores/excepciones en `exception/`.
- Código preparado para facilitar siguiente transición completa a JPA.

---

## Pruebas y mantenimiento

- Ejecutar tests:
  ```sh
  mvn test
  ```
- Compilar:
  ```sh
  mvn clean compile
  ```

---

## Créditos

Desarrollado por:  
Emmanuel Dulcey  
[Repositorio en GitHub](https://github.com/EmmanuelDulcey/cajaviva)

---
## Pruebas unitarias

Este proyecto incluye pruebas unitarias para validar la lógica de negocio y la persistencia en los **DAOs** y **Services**, implementadas con **JUnit 5** y **Mockito**.

### Ejecutar todas las pruebas
En la raíz del proyecto:
```bash
mvn clean test
