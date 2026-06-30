package com.dropcl.msinventario.client;

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

    // Verifica si un producto existe en ms-producto
    public boolean productoExiste(Long productoId) {
        try {
            log.info("Verificando existencia de producto id: {} en ms-producto", productoId);
            webClient.get()
                    .uri("/api/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Producto id: {} encontrado en ms-producto", productoId);
            return true;
        } catch (Exception e) {
            log.warn("Producto id: {} no encontrado en ms-producto: {}", productoId, e.getMessage());
            return false;
        }
    }

    // Verifica si una talla existe para un producto en ms-producto
    public boolean tallaExiste(Long productoId, Long tallaId) {
        try {
            log.info("Verificando talla id: {} del producto id: {} en ms-producto", tallaId, productoId);
            webClient.get()
                    .uri("/api/productos/{productoId}/tallas", productoId)
                    .retrieve()
                    .bodyToFlux(Object.class)
                    .filter(talla -> {
                        // Verificamos que la talla exista en la lista
                        return talla.toString().contains("\"id\":" + tallaId);
                    })
                    .blockFirst();
            log.info("Talla id: {} encontrada en ms-producto", tallaId);
            return true;
        } catch (Exception e) {
            log.warn("Talla id: {} no encontrada en ms-producto: {}", tallaId, e.getMessage());
            return false;
        }
    }
}