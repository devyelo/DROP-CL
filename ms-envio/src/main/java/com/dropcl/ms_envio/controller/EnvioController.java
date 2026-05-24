package com.dropcl.ms_envio.controller;

import com.dropcl.ms_envio.dto.EnvioDto;
import com.dropcl.ms_envio.model.Envio;
import com.dropcl.ms_envio.service.EnvioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private static final Logger log = LoggerFactory.getLogger(EnvioController.class);
    private final EnvioService envioService;

    @PostMapping
    public ResponseEntity<Envio> crearEnvio(@Valid @RequestBody EnvioDto dto) {
        log.info("POST /api/envios - Creando envio para la orden ID: {}", dto.getOrdenId());
        Envio nuevoEnvio = envioService.crearEnvio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEnvio);
    }

    @GetMapping
    public ResponseEntity<List<Envio>> obtenerTodos() {
        log.info("GET /api/envios - Obteniendo todos los envios");
        return ResponseEntity.ok(envioService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/envios/{} - Buscando envio por ID", id);
        return ResponseEntity.ok(envioService.obtenerPorId(id));
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<Envio> obtenerPorOrdenId(@PathVariable Long ordenId) {
        log.info("GET /api/envios/orden/{} - Buscando envio por ID de orden", ordenId);
        return ResponseEntity.ok(envioService.obtenerPorOrdenId(ordenId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Envio> actualizarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        log.info("PUT /api/envios/{}/estado - Actualizando estado a: {}", id, nuevoEstado);
        Envio envioActualizado = envioService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(envioActualizado);
    }

    @PutMapping("/{id}/seguimiento")
    public ResponseEntity<Envio> asignarSeguimiento(@PathVariable Long id, @RequestParam String numeroSeguimiento) {
        log.info("PUT /api/envios/{}/seguimiento - Asignando numero de seguimiento: {}", id, numeroSeguimiento);
        Envio envioActualizado = envioService.asignarSeguimiento(id, numeroSeguimiento);
        return ResponseEntity.ok(envioActualizado);
    }
}