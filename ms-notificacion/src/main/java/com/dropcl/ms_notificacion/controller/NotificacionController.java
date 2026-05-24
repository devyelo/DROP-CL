package com.dropcl.ms_notificacion.controller;

import com.dropcl.ms_notificacion.dto.NotificacionDto;
import com.dropcl.ms_notificacion.model.Notificacion;
import com.dropcl.ms_notificacion.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private static final Logger log = LoggerFactory.getLogger(NotificacionController.class);

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<Notificacion> crear(@Valid @RequestBody NotificacionDto dto) {
        log.info("POST /api/notificaciones - Creando notificacion para usuario {}", dto.getUsuarioId());
        Notificacion creada = notificacionService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacion>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}", usuarioId);
        List<Notificacion> notificaciones = notificacionService.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(notificaciones);
    }

    @GetMapping("/usuario/{usuarioId}/no-leidas")
    public ResponseEntity<List<Notificacion>> obtenerNoLeidas(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/no-leidas", usuarioId);
        List<Notificacion> noLeidas = notificacionService.obtenerNoLeidas(usuarioId);
        return ResponseEntity.ok(noLeidas);
    }

    @GetMapping("/usuario/{usuarioId}/contar")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}/contar", usuarioId);
        long cantidad = notificacionService.contarNoLeidas(usuarioId);
        return ResponseEntity.ok(cantidad);
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Notificacion> marcarComoLeida(@PathVariable Long id) {
        log.info("PUT /api/notificaciones/{}/leer", id);
        Notificacion actualizada = notificacionService.marcarComoLeida(id);
        return ResponseEntity.ok(actualizada);
    }

    @PutMapping("/usuario/{usuarioId}/leer-todas")
    public ResponseEntity<Void> marcarTodasComoLeidas(@PathVariable Long usuarioId) {
        log.info("PUT /api/notificaciones/usuario/{}/leer-todas", usuarioId);
        notificacionService.marcarTodasComoLeidas(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{}", id);
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}