package com.dropcl.msorden.controller;

import com.dropcl.msorden.dto.*;
import com.dropcl.msorden.model.Orden;
import com.dropcl.msorden.service.OrdenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private static final Logger log = LoggerFactory.getLogger(OrdenController.class);
    private final OrdenService ordenService;


    @PostMapping
    public ResponseEntity<?> crearOrden(@Valid @RequestBody CrearOrdenDTO dto) {
        log.info("Petición para crear orden para compradorId: {}", dto.getCompradorId());
        try {
            Orden orden = ordenService.crearOrden(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(orden);
        } catch (RuntimeException e) {
            log.error("Error al crear orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Petición para obtener orden con id: {}", id);
        try {
            return ResponseEntity.ok(ordenService.obtenerPorId(id));
        } catch (RuntimeException e) {
            log.error("Error al obtener orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/comprador/{compradorId}")
    public ResponseEntity<List<Orden>> obtenerPorComprador(@PathVariable Long compradorId) {
        log.info("Petición para obtener órdenes del comprador id: {}", compradorId);
        return ResponseEntity.ok(ordenService.obtenerPorComprador(compradorId));
    }


    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Orden>> obtenerPorVendedor(@PathVariable Long vendedorId) {
        log.info("Petición para obtener órdenes del vendedor id: {}", vendedorId);
        return ResponseEntity.ok(ordenService.obtenerPorVendedor(vendedorId));
    }


    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPorEstado(@PathVariable String estado) {
        log.info("Petición para obtener órdenes con estado: {}", estado);
        try {
            return ResponseEntity.ok(ordenService.obtenerPorEstado(estado));
        } catch (RuntimeException e) {
            log.error("Error al obtener órdenes por estado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                              @Valid @RequestBody ActualizarOrdenDTO dto) {
        log.info("Petición para actualizar estado de orden id: {}", id);
        try {
            return ResponseEntity.ok(ordenService.actualizarEstado(id, dto));
        } catch (RuntimeException e) {
            log.error("Error al actualizar estado de orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarOrden(@PathVariable Long id) {
        log.info("Petición para cancelar orden id: {}", id);
        try {
            return ResponseEntity.ok(ordenService.cancelarOrden(id));
        } catch (RuntimeException e) {
            log.error("Error al cancelar orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}