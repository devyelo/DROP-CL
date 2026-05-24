package com.dropcl.mslisting.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UsuarioClient {

    private static final Logger log = LoggerFactory.getLogger(UsuarioClient.class);
    private final WebClient webClient;

    public UsuarioClient(@Value("${ms.usuario.url}") String usuarioUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(usuarioUrl)
                .build();
    }

    public boolean usuarioExiste(Long usuarioId) {
        try {
            log.info("Verificando usuario id: {} en ms-usuario", usuarioId);
            webClient.get()
                    .uri("/api/perfiles/usuario/{id}", usuarioId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (Exception e) {
            log.warn("Usuario id: {} no encontrado en ms-usuario", usuarioId);
            return false;
        }
    }
}