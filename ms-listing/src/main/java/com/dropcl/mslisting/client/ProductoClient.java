package com.dropcl.mslisting.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ProductoClient {

    private static final Logger log = LoggerFactory.getLogger(ProductoClient.class);
    private final WebClient webClient;

    public ProductoClient(@Value("${ms.producto.url}") String productoUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(productoUrl)
                .build();
    }

    public boolean productoExiste(Long productoId) {
        try {
            log.info("Verificando producto id: {} en ms-producto", productoId);
            webClient.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Producto id: {} no encontrado en ms-producto", productoId);
            return false;
        }
    }
}