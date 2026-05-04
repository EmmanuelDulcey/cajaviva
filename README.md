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

La app lee las siguientes variables de entorno o valores por defecto:

- `DB_URL` (por defecto: `jdbc:sqlserver://localhost:1433;databaseName=CajaViva;encrypt=true;trustServerCertificate=true`)
- `DB_USER` (por defecto: `sa`)
- `DB_PASSWORD` (por defecto: `admin`)

Para cambiar la configuración, sobrescribe variables de entorno antes de iniciar la app.

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

4. **Lanza la aplicación**
   - Con Maven:
     ```sh
     mvn spring-boot:run
     ```
   - O con wrapper:
     ```sh
     ./mvnw.cmd spring-boot:run
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
