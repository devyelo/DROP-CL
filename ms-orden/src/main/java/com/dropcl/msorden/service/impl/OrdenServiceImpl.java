package com.dropcl.msorden.service.impl;

import com.dropcl.msorden.client.*;
import com.dropcl.msorden.dto.*;
import com.dropcl.msorden.model.Orden;
import com.dropcl.msorden.repository.OrdenRepository;
import com.dropcl.msorden.service.OrdenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrdenServiceImpl implements OrdenService {

    private static final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    private final OrdenRepository ordenRepository;
    private final ListingClient listingClient;
    private final InventarioClient inventarioClient;
    private final UsuarioClient usuarioClient;

    @Override
    public Orden crearOrden(CrearOrdenDTO dto) {
        log.info("Creando orden para compradorId: {} listingId: {}",
                dto.getCompradorId(), dto.getListingId());

        // Regla de negocio: verificar que el comprador existe
        if (!usuarioClient.usuarioExiste(dto.getCompradorId())) {
            log.warn("Comprador id: {} no existe en ms-usuario", dto.getCompradorId());
            throw new RuntimeException("El comprador no existe en el sistema");
        }

        // Regla de negocio: verificar que el listing existe y está activo
        if (!listingClient.listingExisteYActivo(dto.getListingId())) {
            log.warn("Listing id: {} no existe o no está activo", dto.getListingId());
            throw new RuntimeException("El listing no existe o no está disponible");
        }

        // Regla de negocio: no puede haber dos órdenes para el mismo listing
        if (ordenRepository.existsByListingId(dto.getListingId())) {
            log.warn("Ya existe una orden para el listing id: {}", dto.getListingId());
            throw new RuntimeException("Ya existe una orden para este listing");
        }

        // Obtener datos del listing
        Map listing = listingClient.obtenerListing(dto.getListingId());
        if (listing == null) {
            throw new RuntimeException("No se pudo obtener la información del listing");
        }

        Long productoId = Long.valueOf(listing.get("productoId").toString());
        Long tallaId = Long.valueOf(listing.get("tallaId").toString());
        Long vendedorId = Long.valueOf(listing.get("vendedorId").toString());
        String tallaValor = listing.get("tallaValor").toString();
        BigDecimal precio = new BigDecimal(listing.get("precio").toString());

        // Regla de negocio: el comprador no puede comprarle a sí mismo
        if (dto.getCompradorId().equals(vendedorId)) {
            log.warn("El comprador id: {} intenta comprarse a sí mismo", dto.getCompradorId());
            throw new RuntimeException("No puedes comprar tu propio listing");
        }

        // Regla de negocio: verificar stock disponible
        if (!inventarioClient.verificarDisponibilidad(productoId, tallaId, 1)) {
            log.warn("Sin stock para productoId: {} tallaId: {}", productoId, tallaId);
            throw new RuntimeException("No hay stock disponible para este producto");
        }

        // Crear la orden
        Orden orden = Orden.builder()
                .compradorId(dto.getCompradorId())
                .vendedorId(vendedorId)
                .listingId(dto.getListingId())
                .productoId(productoId)
                .tallaId(tallaId)
                .tallaValor(tallaValor)
                .precio(precio)
                .direccionEnvio(dto.getDireccionEnvio())
                .notas(dto.getNotas())
                .build();

        Orden guardada = ordenRepository.save(orden);
        log.info("Orden creada con id: {}", guardada.getId());

        // Marcar listing como vendido y descontar stock
        listingClient.marcarListingComoVendido(dto.getListingId());
        inventarioClient.descontarStock(productoId, tallaId);

        log.info("Listing marcado como vendido y stock descontado para orden id: {}", guardada.getId());
        return guardada;
    }

    @Override
    public Orden obtenerPorId(Long id) {
        log.info("Buscando orden con id: {}", id);
        return ordenRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Orden no encontrada con id: {}", id);
                    return new RuntimeException("Orden no encontrada con id: " + id);
                });
    }

    @Override
    public List<Orden> obtenerPorComprador(Long compradorId) {
        log.info("Obteniendo órdenes del comprador id: {}", compradorId);
        return ordenRepository.findByCompradorId(compradorId);
    }

    @Override
    public List<Orden> obtenerPorVendedor(Long vendedorId) {
        log.info("Obteniendo órdenes del vendedor id: {}", vendedorId);
        return ordenRepository.findByVendedorId(vendedorId);
    }

    @Override
    public List<Orden> obtenerPorEstado(String estado) {
        log.info("Obteniendo órdenes con estado: {}", estado);
        try {
            Orden.EstadoOrden estadoEnum = Orden.EstadoOrden.valueOf(estado.toUpperCase());
            return ordenRepository.findByEstado(estadoEnum);
        } catch (IllegalArgumentException e) {
            log.warn("Estado inválido: {}", estado);
            throw new RuntimeException("Estado inválido: " + estado);
        }
    }

    @Override
    public Orden actualizarEstado(Long id, ActualizarOrdenDTO dto) {
        log.info("Actualizando estado de orden id: {} a {}", id, dto.getEstado());

        Orden orden = obtenerPorId(id);

        // Regla de negocio: no se puede modificar una orden cancelada
        if (orden.getEstado() == Orden.EstadoOrden.CANCELADA) {
            log.warn("Intento de modificar orden cancelada id: {}", id);
            throw new RuntimeException("No se puede modificar una orden cancelada");
        }

        // Regla de negocio: no se puede retroceder el estado
        Orden.EstadoOrden nuevoEstado = Orden.EstadoOrden.valueOf(dto.getEstado());
        orden.setEstado(nuevoEstado);

        if (dto.getNotas() != null) orden.setNotas(dto.getNotas());

        Orden actualizada = ordenRepository.save(orden);
        log.info("Orden id: {} actualizada a estado: {}", id, actualizada.getEstado());
        return actualizada;
    }

    @Override
    public Orden cancelarOrden(Long id) {
        log.info("Cancelando orden id: {}", id);

        Orden orden = obtenerPorId(id);

        // Regla de negocio: solo se puede cancelar si está pendiente
        if (orden.getEstado() != Orden.EstadoOrden.PENDIENTE) {
            log.warn("Intento de cancelar orden en estado: {}", orden.getEstado());
            throw new RuntimeException("Solo se pueden cancelar órdenes en estado PENDIENTE");
        }

        orden.setEstado(Orden.EstadoOrden.CANCELADA);
        Orden cancelada = ordenRepository.save(orden);
        log.info("Orden id: {} cancelada correctamente", id);
        return cancelada;
    }
}