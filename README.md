# CajaViva

CajaViva es una API backend desarrollada con Spring Boot para la gestion de informacion financiera. El proyecto modela usuarios, cuentas, categorias, transacciones financieras, transacciones recurrentes, proyecciones de liquidez y alertas.

Actualmente el proyecto usa:

- Java 17
- Spring Boot 3
- SQL Server
- Spring Data JPA
- Flyway para migraciones
- Springdoc OpenAPI para documentacion

## Estructura general

La aplicacion esta organizada en capas para separar responsabilidades:

- `controller/`: expone los endpoints REST.
- `entity/`: representa el modelo persistido en base de datos.
- `dao/`: define los contratos de acceso a datos.
- `dao/jpa/`: adaptadores tecnicos basados en `JpaRepository`.
- `dao/impl/`: implementaciones concretas de los DAO.
- `exception/`: manejo centralizado de errores.
- `src/main/resources/db/migration/`: migraciones Flyway.

## Requisitos

Antes de levantar el proyecto, asegúrate de tener instalado:

- Java 17
- Maven o usar el wrapper incluido (`mvnw.cmd`)
- SQL Server
- SQL Server Management Studio (recomendado para configurar autenticacion e instancia)

## Configuracion de variables

La aplicacion usa estas variables de entorno para conectarse a SQL Server:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

Si no las defines, Spring Boot usa estos valores por defecto:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=CajaViva;encrypt=true;trustServerCertificate=true
    username: sa
    password: admin
```

## Como levantar el proyecto

1. Clona el repositorio:

```bash
git clone https://github.com/EmmanuelDulcey/cajaviva.git
cd cajaviva
```

2. Crea o prepara una base de datos llamada `CajaViva` en tu instancia de SQL Server.

3. Configura las credenciales de base de datos.

En PowerShell, por ejemplo:

```powershell
$env:DB_URL="jdbc:sqlserver://localhost:1433;databaseName=CajaViva;encrypt=true;trustServerCertificate=true"
$env:DB_USER="sa"
$env:DB_PASSWORD="tu_password"
```

4. Levanta la aplicacion:

Con Maven instalado:

```bash
mvn spring-boot:run
```

O con el wrapper:

```bash
./mvnw.cmd spring-boot:run
```

5. Una vez inicie correctamente, la aplicacion quedara disponible en:

- API base: `http://localhost:8080/v1`
- Swagger UI: `http://localhost:8080/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v1/api-docs`

## Migraciones

Flyway esta habilitado en el proyecto. Al levantar la aplicacion, ejecuta automaticamente las migraciones ubicadas en:

`src/main/resources/db/migration`

La migracion inicial crea las tablas y relaciones base del proyecto.

## Consideraciones importantes

> [!IMPORTANT]
> ### Configuracion protocolo TCP/IP
>
> Para que SQL Server acepte la conexion desde Spring Boot es necesario configurar el protocolo TCP/IP en la instancia que se usara. Pueden seguir [esta guia](https://docs.trellix.com/es-ES/bundle/epolicy-orchestrator-5.10.0-installation-guide/page/UUID-d6e07f40-623d-a533-7d4e-3398f7e7a3d5.html) para configurarlo o preguntarle al yepeto.
>
> Ademas, si estan usando una instancia diferente a la por defecto de SQL Server, asegúrense de que este habilitado el protocolo TCP/IP para esa instancia y si esta configurado en un puerto diferente al default (`1433`) tambien asegúrense de pasar ese puerto en el string de conexion.
>
> ### Autenticacion
>
> Es probable que al levantar el servidor les falle por autenticacion. Para solucionar esto se debe activar el modo mixto de autenticacion en el servidor de SQL Server:
>
> 1. En SSMS, haz clic derecho sobre el nombre de tu servidor.
> 2. Selecciona `Properties`.
> 3. Ve a la seccion `Security`.
> 4. Cambia la opcion a `SQL Server and Windows Authentication mode`.
> 5. Dale a `OK`.
> 6. En el explorador de objetos de SSMS, despliega `Security > Logins`.
> 7. Haz clic derecho en `sa > Properties`.
> 8. En `General`, escribe una contraseña nueva y asegúrate de que no este marcada la casilla `Enforce password policy`.
> 9. En `Status`, asegúrate de que en `Settings` diga `Enabled` y en `Login` diga `Grant`.
> 10. Dale a `OK`.
> 11. Vuelve al SQL Server Configuration Manager.
> 12. En `SQL Server Services`, haz clic derecho en tu servicio de SQL Server y dale a `Restart`.
>
> Con esto configurado, no deberian tener problemas para levantar el proyecto en local y al iniciar la aplicacion se deberian crear todas las tablas definidas en la migracion inicial.

## Notas sobre arquitectura

La base actual del proyecto ya esta preparada para seguir una arquitectura por capas:

- Los controladores no deberian contener logica de negocio.
- La logica de negocio debe vivir en `service/`.
- La capa `service/` debe depender de interfaces DAO, no de `JpaRepository`.
- Los `JpaRepository` dentro de `dao/jpa/` son detalles de infraestructura.
- Las implementaciones en `dao/impl/` conectan el contrato DAO con Spring Data JPA.
- El paquete `exception/` centraliza errores comunes y respuestas consistentes.

## Problemas comunes

Si la aplicacion no levanta, revisa primero:

- que SQL Server este corriendo,
- que la base de datos `CajaViva` exista,
- que el puerto del string de conexion sea correcto,
- que TCP/IP este habilitado,
- que el usuario configurado tenga acceso,
- que Flyway pueda conectarse correctamente a la base de datos.

## Comandos utiles

Ejecutar la aplicacion:

```bash
mvn spring-boot:run
```

Ejecutar tests:

```bash
mvn test
```

Compilar el proyecto:

```bash
mvn clean compile
```
