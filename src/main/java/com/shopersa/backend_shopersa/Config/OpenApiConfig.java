package com.shopersa.backend_shopersa.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Shopersa API", version = "v1", description = "Backend de Shopersa"),
    servers = { @Server(url = "http://localhost:8080") },
    security = { @SecurityRequirement(name = "bearerAuth") }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    GroupedOpenApi auth() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    GroupedOpenApi usuarios() {
        return GroupedOpenApi.builder()
                .group("usuarios")
                .pathsToMatch("/api/usuarios/**")
                .build();
    }

    @Bean
    GroupedOpenApi productos() {
        return GroupedOpenApi.builder()
                .group("productos")
                .pathsToMatch("/api/productos/**")
                .build();
    }

    @Bean
    GroupedOpenApi categorias() {
        return GroupedOpenApi.builder()
                .group("categorias")
                .pathsToMatch("/api/categorias/**")
                .build();
    }

    @Bean
    GroupedOpenApi ventas() {
        return GroupedOpenApi.builder()
                .group("ventas")
                .pathsToMatch("/api/ventas/**")
                .build();
    }

    @Bean
    GroupedOpenApi envios() {
        return GroupedOpenApi.builder()
                .group("envios")
                .pathsToMatch("/api/envios/**")
                .build();
    }

    @Bean
    GroupedOpenApi permisos() {
        return GroupedOpenApi.builder()
                .group("permisos")
                .pathsToMatch("/api/permisos/**")
                .build();
    }

    @Bean
    GroupedOpenApi detalleVenta() {
        return GroupedOpenApi.builder()
                .group("detalle-venta")
                .pathsToMatch("/api/detalles-venta/**")
                .build();
    }
}
