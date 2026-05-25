package com.dropcl.ms_pago.controller;

import com.dropcl.ms_pago.dto.PagoDto;
import com.dropcl.ms_pago.model.Pago;
import com.dropcl.ms_pago.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);
    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<Pago> crearPago(@Valid @RequestBody PagoDto dto) {
        log.info("POST /api/pagos - Creando pago para orden ID: {}", dto.getOrdenId());
        Pago nuevoPago = pagoService.crearPago(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPago);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/pagos/{} - Buscando pago", id);
        return ResponseEntity.ok(pagoService.obtenerPorId(id));
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<Pago>> obtenerPorOrdenId(@PathVariable Long ordenId) {
        log.info("GET /api/pagos/orden/{} - Buscando pagos de la orden", ordenId);
        return ResponseEntity.ok(pagoService.obtenerPorOrdenId(ordenId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado,
            @RequestParam(required = false) String codigoTransaccion) {

        log.info("PUT /api/pagos/{}/estado - Actualizando estado a: {}", id, nuevoEstado);
        Pago pagoActualizado = pagoService.actualizarEstado(id, nuevoEstado, codigoTransaccion);
        return ResponseEntity.ok(pagoActualizado);
    }
}