package com.dropcl.mslisting.service;

import com.dropcl.mslisting.dto.*;
import com.dropcl.mslisting.model.Listing;
import java.util.List;

public interface ListingService {

    // Crear publicación
    Listing crearListing(CrearListingDTO dto);

    // Obtener por id
    Listing obtenerPorId(Long id);

    // Obtener todos los listings activos
    List<Listing> obtenerActivos();

    // Obtener listings de un vendedor
    List<Listing> obtenerPorVendedor(Long vendedorId);

    // Obtener listings de un producto
    List<Listing> obtenerPorProducto(Long productoId);

    // Obtener por tipo de venta
    List<Listing> obtenerPorTipoVenta(String tipoVenta);

    // Actualizar listing
    Listing actualizarListing(Long id, ActualizarListingDTO dto);

    // Marcar como vendido
    Listing marcarComoVendido(Long id);

    // Cancelar listing
    void cancelarListing(Long id);
}