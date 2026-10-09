package com.duoc.rutalimpia.solicitudes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String BEARER = "bearerAuth";
    public static final String INTERNAL = "internalKey";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RutaLimpia - Solicitudes API")
                        .version("v1")
                        .description("Microservicio de solicitudes de retiro de reciclaje. "
                                + "El vecino crea y consulta sus solicitudes; al crearla queda en estado PENDIENTE "
                                + "con un folio de recepción. Los endpoints /internal son solo para otros servicios."))
                .components(new Components()
                        .addSecuritySchemes(BEARER, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT emitido por auth-service (rol VECINO)"))
                        .addSecuritySchemes(INTERNAL, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-Internal-Key")
                                .description("Clave para llamadas entre servicios")));
    }
}
