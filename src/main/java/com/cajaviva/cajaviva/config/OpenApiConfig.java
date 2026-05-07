package com.cajaviva.cajaviva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cajaVivaAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Caja Viva API")
                        .version("1.0")
                        .description("""
                                API para gestion financiera.

                                Flujo de autenticacion:
                                1. Invoca POST /auth/login con email y password.
                                2. El backend responde con Set-Cookie para CV_ACCESS_TOKEN y CV_REFRESH_TOKEN.
                                3. Usa endpoints protegidos enviando cookies automaticamente.
                                4. Cuando expire el access token, invoca POST /auth/refresh.
                                5. Para cerrar sesion invoca POST /auth/logout.

                                Para operaciones mutables (POST/PUT/PATCH/DELETE) envia CSRF token (header X-XSRF-TOKEN).
                                En Swagger UI o escenarios cross-origin puede requerirse withCredentials y CORS compatible con cookies.
                                """)
                        .contact(new Contact()
                                .name("Equipo Caja Viva")
                                .email("soporte@cajaviva.com")))
                .schemaRequirement("cookieAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("CV_ACCESS_TOKEN")
                                .description("Cookie HttpOnly con JWT de acceso"))
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
    }
}
