package com.dropcl.ms_oferta.controller;

import com.dropcl.ms_oferta.dto.OfertaDto;
import com.dropcl.ms_oferta.model.Oferta;
import com.dropcl.ms_oferta.service.OfertaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ofertas")
@RequiredArgsConstructor
public class OfertaController {

    private static final Logger log = LoggerFactory.getLogger(OfertaController.class);
    private final OfertaService ofertaService;

    @PostMapping
    public ResponseEntity<Oferta> crearOferta(@Valid @RequestBody OfertaDto dto) {
        log.info("POST /api/ofertas - Creando oferta para el producto ID: {}", dto.getProductoId());
        Oferta nuevaOferta = ofertaService.crearOferta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOferta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Oferta> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/ofertas/{} - Buscando oferta", id);
        return ResponseEntity.ok(ofertaService.obtenerPorId(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Oferta>> obtenerPorProducto(@PathVariable Long productoId) {
        log.info("GET /api/ofertas/producto/{} - Buscando ofertas del producto", productoId);
        return ResponseEntity.ok(ofertaService.obtenerPorProducto(productoId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Oferta>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/ofertas/usuario/{} - Buscando ofertas del usuario", usuarioId);
        return ResponseEntity.ok(ofertaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/producto/{productoId}/mas-alta")
    public ResponseEntity<Oferta> obtenerOfertaMasAlta(@PathVariable Long productoId) {
        log.info("GET /api/ofertas/producto/{}/mas-alta - Buscando la oferta mas alta", productoId);
        return ResponseEntity.ok(ofertaService.obtenerOfertaMasAlta(productoId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Oferta> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {

        log.info("PUT /api/ofertas/{}/estado - Actualizando estado a: {}", id, nuevoEstado);
        Oferta ofertaActualizada = ofertaService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(ofertaActualizada);
    }
}