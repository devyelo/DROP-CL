package com.dropcl.msinventario.service;

import com.dropcl.msinventario.dto.*;
import com.dropcl.msinventario.model.Stock;
import java.util.List;

public interface StockService {

    // Crear registro de stock
    Stock crearStock(CrearStockDTO dto);

    // Obtener stock por id
    Stock obtenerPorId(Long id);

    // Obtener todo el stock de un producto
    List<Stock> obtenerPorProducto(Long productoId);

    // Obtener stock por producto y talla
    Stock obtenerPorProductoYTalla(Long productoId, Long tallaId);

    // Actualizar cantidad de stock
    Stock actualizarStock(Long id, ActualizarStockDTO dto);

    // Ajustar stock (entrada o salida)
    Stock ajustarStock(Long id, AjusteStockDTO dto);

    // Obtener stock por estado
    List<Stock> obtenerPorEstado(Stock.EstadoStock estado);

    // Verificar disponibilidad
    boolean verificarDisponibilidad(Long productoId, Long tallaId, Integer cantidad);
}