package com.cajaviva.cajaviva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
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
                        .description("API para gestión financiera")
                        .contact(new Contact()
                                .name("Equipo Caja Viva")
                                .email("soporte@cajaviva.com")));
    }
}