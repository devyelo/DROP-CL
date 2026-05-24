package com.dropcl.msorden.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

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
            log.info("Disponibilidad → {}", disponible);
            return Boolean.TRUE.equals(disponible);
        } catch (Exception e) {
            log.warn("Error verificando disponibilidad: {}", e.getMessage());
            return false;
        }
    }

    public void descontarStock(Long productoId, Long tallaId) {
        try {
            log.info("Descontando stock para productoId: {} tallaId: {}", productoId, tallaId);

            // Primero obtenemos el stock
            Map stock = webClient.get()
                    .uri("/api/inventario/producto/{productoId}/talla/{tallaId}",
                            productoId, tallaId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (stock != null) {
                Long stockId = Long.valueOf(stock.get("id").toString());

                // Descontamos 1 unidad
                webClient.patch()
                        .uri("/api/inventario/{id}/ajuste", stockId)
                        .bodyValue(Map.of("cantidad", -1, "motivo", "Venta confirmada por orden"))
                        .retrieve()
                        .bodyToMono(Object.class)
                        .block();

                log.info("Stock descontado correctamente para productoId: {} tallaId: {}",
                        productoId, tallaId);
            }
        } catch (Exception e) {
            log.error("Error descontando stock: {}", e.getMessage());
        }
    }
}
