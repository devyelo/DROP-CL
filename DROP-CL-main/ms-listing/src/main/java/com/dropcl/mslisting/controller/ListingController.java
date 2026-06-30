package com.dropcl.mslisting.controller;

import com.dropcl.mslisting.dto.*;
import com.dropcl.mslisting.model.Listing;
import com.dropcl.mslisting.service.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private static final Logger log = LoggerFactory.getLogger(ListingController.class);
    private final ListingService listingService;

    // POST /api/listings
    @PostMapping
    public ResponseEntity<?> crearListing(@Valid @RequestBody CrearListingDTO dto) {
        log.info("Petición para crear listing para vendedorId: {}", dto.getVendedorId());
        try {
            Listing listing = listingService.crearListing(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(listing);
        } catch (RuntimeException e) {
            log.error("Error al crear listing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // GET /api/listings
    @GetMapping
    public ResponseEntity<List<Listing>> obtenerActivos() {
        log.info("Petición para obtener listings activos");
        return ResponseEntity.ok(listingService.obtenerActivos());
    }

    // GET /api/listings/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Petición para obtener listing con id: {}", id);
        try {
            return ResponseEntity.ok(listingService.obtenerPorId(id));
        } catch (RuntimeException e) {
            log.error("Error al obtener listing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // GET /api/listings/vendedor/{vendedorId}
    @GetMapping("/vendedor/{vendedorId}")
    public ResponseEntity<List<Listing>> obtenerPorVendedor(@PathVariable Long vendedorId) {
        log.info("Petición para obtener listings del vendedor id: {}", vendedorId);
        return ResponseEntity.ok(listingService.obtenerPorVendedor(vendedorId));
    }

    // GET /api/listings/producto/{productoId}
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<Listing>> obtenerPorProducto(@PathVariable Long productoId) {
        log.info("Petición para obtener listings del producto id: {}", productoId);
        return ResponseEntity.ok(listingService.obtenerPorProducto(productoId));
    }

    // GET /api/listings/tipo/{tipoVenta}
    @GetMapping("/tipo/{tipoVenta}")
    public ResponseEntity<?> obtenerPorTipoVenta(@PathVariable String tipoVenta) {
        log.info("Petición para obtener listings por tipo de venta: {}", tipoVenta);
        try {
            return ResponseEntity.ok(listingService.obtenerPorTipoVenta(tipoVenta));
        } catch (RuntimeException e) {
            log.error("Error al obtener listings por tipo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // PUT /api/listings/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarListing(@PathVariable Long id,
                                               @Valid @RequestBody ActualizarListingDTO dto) {
        log.info("Petición para actualizar listing id: {}", id);
        try {
            return ResponseEntity.ok(listingService.actualizarListing(id, dto));
        } catch (RuntimeException e) {
            log.error("Error al actualizar listing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // PATCH /api/listings/{id}/vendido
    @PatchMapping("/{id}/vendido")
    public ResponseEntity<?> marcarComoVendido(@PathVariable Long id) {
        log.info("Petición para marcar listing id: {} como vendido", id);
        try {
            return ResponseEntity.ok(listingService.marcarComoVendido(id));
        } catch (RuntimeException e) {
            log.error("Error al marcar como vendido: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // DELETE /api/listings/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarListing(@PathVariable Long id) {
        log.info("Petición para cancelar listing id: {}", id);
        try {
            listingService.cancelarListing(id);
            return ResponseEntity.ok("Listing cancelado correctamente");
        } catch (RuntimeException e) {
            log.error("Error al cancelar listing: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}