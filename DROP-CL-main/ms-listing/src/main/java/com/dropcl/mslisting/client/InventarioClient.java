package com.dropcl.mslisting.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InventarioClient {

    private static final Logger log = LoggerFactory.getLogger(InventarioClient.class);
    private final WebClient webClient;

    public InventarioClient(@Value("${ms.inventario.url}") String inventarioUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(inventarioUrl)
                .build();
    }

    public boolean verificarDisponibilidad(Long productoId, Long tallaId, Integer cantidad) {
        try {
            log.info("Verificando disponibilidad en ms-inventario — productoId: {} tallaId: {}",
                    productoId, tallaId);
            Boolean disponible = webClient.get()
                    .uri("/api/inventario/disponibilidad?productoId={p}&tallaId={t}&cantidad={c}",
                            productoId, tallaId, cantidad)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            log.info("Disponibilidad para productoId: {} tallaId: {} → {}", productoId, tallaId, disponible);
            return Boolean.TRUE.equals(disponible);
        } catch (Exception e) {
            log.warn("Error verificando disponibilidad en ms-inventario: {}", e.getMessage());
            return false;
        }
    }
}