# CajaViva

CajaViva es un sistema de gestión financiera desarrollado como proyecto académico en la Universidad.  
El objetivo es modelar y administrar cuentas, transacciones, proyecciones de liquidez y alertas, garantizando consistencia en los datos mediante constraints y buenas prácticas de diseño.

## 🚀 Características principales
- Tablas en plural con claves primarias semánticas (`person_id`, `account_id`, etc.).
- Uso de `NEWSEQUENTIALID()` para mejorar el rendimiento de índices en UUIDs.
- Constraints de integridad:
    - Valores no negativos en balances y transacciones.
    - Validación de fechas (`end_date >= initial_date`).
- Índices únicos en campos clave (`email`, `name`).
- Relaciones completas entre entidades (`Accounts`, `Categories`, `FinancialTransactions`, `RecurrentTransactions`, etc.).

## 📂 Estructura del proyecto
- `src/` → Código fuente de la API.
- `migrations/` → Scripts SQL gestionados con Flyway.
- `pom.xml` → Configuración del proyecto Maven.
- `.gitignore` → Archivos y carpetas ignoradas por Git.

## 🛠️ Tecnologías
- **Java / Spring Boot** para la API.
- **SQL Server** para la base de datos.
- **Flyway** para la gestión de migraciones.
- **GitHub** para control de versiones.

## ⚙️ Instalación y uso
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/EmmanuelDulcey/cajaviva.git
2. Configurar la base de datos en SQL Server.
3. Ejecutar las migraciones con Flyway.
4. Levantar la API con Maven: mvn spring-boot:run
