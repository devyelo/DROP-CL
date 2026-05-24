package com.dropcl.mslisting.service.impl;

import com.dropcl.mslisting.client.*;
import com.dropcl.mslisting.dto.*;
import com.dropcl.mslisting.model.Listing;
import com.dropcl.mslisting.repository.ListingRepository;
import com.dropcl.mslisting.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private static final Logger log = LoggerFactory.getLogger(ListingServiceImpl.class);

    private final ListingRepository listingRepository;
    private final ProductoClient productoClient;
    private final InventarioClient inventarioClient;
    private final UsuarioClient usuarioClient;

    @Override
    public Listing crearListing(CrearListingDTO dto) {
        log.info("Creando listing para vendedorId: {} productoId: {}", dto.getVendedorId(), dto.getProductoId());

        // Regla de negocio: verificar que el usuario existe en ms-usuario
        if (!usuarioClient.usuarioExiste(dto.getVendedorId())) {
            log.warn("Vendedor id: {} no existe en ms-usuario", dto.getVendedorId());
            throw new RuntimeException("El vendedor no existe en el sistema");
        }

        // Regla de negocio: verificar que el producto existe en ms-producto
        if (!productoClient.productoExiste(dto.getProductoId())) {
            log.warn("Producto id: {} no existe en ms-producto", dto.getProductoId());
            throw new RuntimeException("El producto no existe en el sistema");
        }

        // Regla de negocio: verificar disponibilidad en ms-inventario
        if (!inventarioClient.verificarDisponibilidad(dto.getProductoId(), dto.getTallaId(), 1)) {
            log.warn("Sin stock para productoId: {} tallaId: {}", dto.getProductoId(), dto.getTallaId());
            throw new RuntimeException("No hay stock disponible para este producto y talla");
        }

        // Regla de negocio: no puede publicar el mismo producto/talla dos veces activo
        if (listingRepository.existsByVendedorIdAndProductoIdAndTallaIdAndEstado(
                dto.getVendedorId(), dto.getProductoId(), dto.getTallaId(), Listing.EstadoListing.ACTIVO)) {
            log.warn("Ya existe un listing activo para este vendedor/producto/talla");
            throw new RuntimeException("Ya tienes una publicación activa para este producto y talla");
        }

        Listing listing = Listing.builder()
                .vendedorId(dto.getVendedorId())
                .productoId(dto.getProductoId())
                .tallaId(dto.getTallaId())
                .tallaValor(dto.getTallaValor())
                .precio(dto.getPrecio())
                .tipoVenta(Listing.TipoVenta.valueOf(dto.getTipoVenta()))
                .condicion(Listing.CondicionProducto.valueOf(dto.getCondicion()))
                .descripcion(dto.getDescripcion())
                .build();

        Listing guardado = listingRepository.save(listing);
        log.info("Listing creado con id: {}", guardado.getId());
        return guardado;
    }

    @Override
    public Listing obtenerPorId(Long id) {
        log.info("Buscando listing con id: {}", id);
        return listingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Listing no encontrado con id: {}", id);
                    return new RuntimeException("Listing no encontrado con id: " + id);
                });
    }

    @Override
    public List<Listing> obtenerActivos() {
        log.info("Obteniendo listings activos");
        return listingRepository.findByEstado(Listing.EstadoListing.ACTIVO);
    }

    @Override
    public List<Listing> obtenerPorVendedor(Long vendedorId) {
        log.info("Obteniendo listings del vendedor id: {}", vendedorId);
        return listingRepository.findByVendedorId(vendedorId);
    }

    @Override
    public List<Listing> obtenerPorProducto(Long productoId) {
        log.info("Obteniendo listings del producto id: {}", productoId);
        return listingRepository.findByProductoId(productoId);
    }

    @Override
    public List<Listing> obtenerPorTipoVenta(String tipoVenta) {
        log.info("Obteniendo listings por tipo de venta: {}", tipoVenta);
        try {
            Listing.TipoVenta tipo = Listing.TipoVenta.valueOf(tipoVenta.toUpperCase());
            return listingRepository.findByTipoVenta(tipo);
        } catch (IllegalArgumentException e) {
            log.warn("Tipo de venta inválido: {}", tipoVenta);
            throw new RuntimeException("Tipo de venta inválido: " + tipoVenta);
        }
    }

    @Override
    public Listing actualizarListing(Long id, ActualizarListingDTO dto) {
        log.info("Actualizando listing con id: {}", id);

        Listing listing = obtenerPorId(id);

        // Regla de negocio: no se puede modificar un listing vendido o cancelado
        if (listing.getEstado() == Listing.EstadoListing.VENDIDO ||
                listing.getEstado() == Listing.EstadoListing.CANCELADO) {
            log.warn("Intento de modificar listing en estado: {}", listing.getEstado());
            throw new RuntimeException("No se puede modificar un listing " + listing.getEstado());
        }

        if (dto.getPrecio() != null) listing.setPrecio(dto.getPrecio());
        if (dto.getDescripcion() != null) listing.setDescripcion(dto.getDescripcion());
        if (dto.getEstado() != null) {
            listing.setEstado(Listing.EstadoListing.valueOf(dto.getEstado()));
        }

        Listing actualizado = listingRepository.save(listing);
        log.info("Listing id: {} actualizado correctamente", actualizado.getId());
        return actualizado;
    }

    @Override
    public Listing marcarComoVendido(Long id) {
        log.info("Marcando listing id: {} como vendido", id);

        Listing listing = obtenerPorId(id);

        // Regla de negocio: solo se puede vender un listing activo
        if (listing.getEstado() != Listing.EstadoListing.ACTIVO) {
            log.warn("Listing id: {} no está activo, estado actual: {}", id, listing.getEstado());
            throw new RuntimeException("Solo se pueden marcar como vendidos listings activos");
        }

        listing.setEstado(Listing.EstadoListing.VENDIDO);
        Listing vendido = listingRepository.save(listing);
        log.info("Listing id: {} marcado como vendido", id);
        return vendido;
    }

    @Override
    public void cancelarListing(Long id) {
        log.info("Cancelando listing id: {}", id);

        Listing listing = obtenerPorId(id);

        // Regla de negocio: no se puede cancelar un listing ya vendido
        if (listing.getEstado() == Listing.EstadoListing.VENDIDO) {
            log.warn("Intento de cancelar listing ya vendido id: {}", id);
            throw new RuntimeException("No se puede cancelar un listing ya vendido");
        }

        listing.setEstado(Listing.EstadoListing.CANCELADO);
        listingRepository.save(listing);
        log.info("Listing id: {} cancelado correctamente", id);
    }
}