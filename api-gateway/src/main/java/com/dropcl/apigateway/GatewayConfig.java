package com.dropcl.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-auth", r -> r
                        .path("/api/auth/**")
                        .uri("http://localhost:8081"))
                .route("ms-usuario", r -> r
                        .path("/api/usuarios/**")
                        .uri("http://localhost:8082"))
                .route("ms-producto", r -> r
                        .path("/api/productos/**")
                        .uri("http://localhost:8083"))
                .route("ms-inventario", r -> r
                        .path("/api/inventario/**")
                        .uri("http://localhost:8084"))
                .route("ms-listing", r -> r
                        .path("/api/listings/**")
                        .uri("http://localhost:8085"))
                .route("ms-oferta", r -> r
                        .path("/api/ofertas/**")
                        .uri("http://localhost:8086"))
                .route("ms-orden", r -> r
                        .path("/api/ordenes/**")
                        .uri("http://localhost:8087"))
                .route("ms-pago", r -> r
                        .path("/api/pagos/**")
                        .uri("http://localhost:8088"))
                .route("ms-envio", r -> r
                        .path("/api/envios/**")
                        .uri("http://localhost:8089"))
                .route("ms-notificacion", r -> r
                        .path("/api/notificaciones/**")
                        .uri("http://localhost:8090"))
                .build();
    }
}