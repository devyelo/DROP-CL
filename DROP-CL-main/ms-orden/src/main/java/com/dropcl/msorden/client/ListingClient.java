package com.dropcl.msorden.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
public class ListingClient {

    private static final Logger log = LoggerFactory.getLogger(ListingClient.class);
    private final WebClient webClient;

    public ListingClient(@Value("${ms.listing.url}") String listingUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(listingUrl)
                .build();
    }

    public boolean listingExisteYActivo(Long listingId) {
        try {
            log.info("Verificando listing id: {} en ms-listing", listingId);
            Map response = webClient.get()
                    .uri("/api/listings/{id}", listingId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            String estado = response != null ? (String) response.get("estado") : null;
            boolean activo = "ACTIVO".equals(estado);
            log.info("Listing id: {} estado: {} activo: {}", listingId, estado, activo);
            return activo;
        } catch (Exception e) {
            log.warn("Listing id: {} no encontrado en ms-listing: {}", listingId, e.getMessage());
            return false;
        }
    }

    public Map obtenerListing(Long listingId) {
        try {
            log.info("Obteniendo datos del listing id: {} en ms-listing", listingId);
            return webClient.get()
                    .uri("/api/listings/{id}", listingId)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            log.warn("Error obteniendo listing id: {}: {}", listingId, e.getMessage());
            return null;
        }
    }

    public void marcarListingComoVendido(Long listingId) {
        try {
            log.info("Marcando listing id: {} como vendido en ms-listing", listingId);
            webClient.patch()
                    .uri("/api/listings/{id}/vendido", listingId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            log.info("Listing id: {} marcado como vendido exitosamente", listingId);
        } catch (Exception e) {
            log.error("Error marcando listing id: {} como vendido: {}", listingId, e.getMessage());
        }
    }
}