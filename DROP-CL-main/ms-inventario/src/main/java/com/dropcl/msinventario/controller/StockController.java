package com.dropcl.msinventario.controller;

import com.dropcl.msinventario.dto.*;
import com.dropcl.msinventario.model.Stock;
import com.dropcl.msinventario.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class StockController {

    private static final Logger log = LoggerFactory.getLogger(StockController.class);
    private final StockService stockService;

    // POST /api/inventario
    @PostMapping
    public ResponseEntity<?> crearStock(@Valid @RequestBody CrearStockDTO dto) {
        log.info("Petición para crear stock para productoId: {}", dto.getProductoId());
        try {
            Stock stock = stockService.crearStock(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(stock);
        } catch (RuntimeException e) {
            log.error("Error al crear stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // GET /api/inventario/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Petición para obtener stock con id: {}", id);
        try {
            return ResponseEntity.ok(stockService.obtenerPorId(id));
        } catch (RuntimeException e) {
            log.error("Error al obtener stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /api/inventario/producto/{productoId}
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Stock>> obtenerPorProducto(@PathVariable Long productoId) {
        log.info("Petición para obtener stock del producto id: {}", productoId);
        return ResponseEntity.ok(stockService.obtenerPorProducto(productoId));
    }

    // GET /api/inventario/producto/{productoId}/talla/{tallaId}
    @GetMapping("/producto/{productoId}/talla/{tallaId}")
    public ResponseEntity<?> obtenerPorProductoYTalla(@PathVariable Long productoId,
                                                      @PathVariable Long tallaId) {
        log.info("Petición para obtener stock de productoId: {} tallaId: {}", productoId, tallaId);
        try {
            return ResponseEntity.ok(stockService.obtenerPorProductoYTalla(productoId, tallaId));
        } catch (RuntimeException e) {
            log.error("Error al obtener stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // PUT /api/inventario/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id,
                                             @Valid @RequestBody ActualizarStockDTO dto) {
        log.info("Petición para actualizar stock id: {}", id);
        try {
            return ResponseEntity.ok(stockService.actualizarStock(id, dto));
        } catch (RuntimeException e) {
            log.error("Error al actualizar stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // PATCH /api/inventario/{id}/ajuste
    @PatchMapping("/{id}/ajuste")
    public ResponseEntity<?> ajustarStock(@PathVariable Long id,
                                          @Valid @RequestBody AjusteStockDTO dto) {
        log.info("Petición para ajustar stock id: {}", id);
        try {
            return ResponseEntity.ok(stockService.ajustarStock(id, dto));
        } catch (RuntimeException e) {
            log.error("Error al ajustar stock: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // GET /api/inventario/estado/{estado}
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> obtenerPorEstado(@PathVariable String estado) {
        log.info("Petición para obtener stock con estado: {}", estado);
        try {
            Stock.EstadoStock estadoEnum = Stock.EstadoStock.valueOf(estado.toUpperCase());
            return ResponseEntity.ok(stockService.obtenerPorEstado(estadoEnum));
        } catch (IllegalArgumentException e) {
            log.error("Estado inválido: {}", estado);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Estado inválido: " + estado);
        }
    }

    // GET /api/inventario/disponibilidad?productoId=&tallaId=&cantidad=
    @GetMapping("/disponibilidad")
    public ResponseEntity<?> verificarDisponibilidad(@RequestParam Long productoId,
                                                     @RequestParam Long tallaId,
                                                     @RequestParam Integer cantidad) {
        log.info("Petición para verificar disponibilidad productoId: {} tallaId: {} cantidad: {}",
                productoId, tallaId, cantidad);
        boolean disponible = stockService.verificarDisponibilidad(productoId, tallaId, cantidad);
        return ResponseEntity.ok(disponible);
    }
}