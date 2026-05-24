package com.dropcl.msorden.service;

import com.dropcl.msorden.dto.*;
import com.dropcl.msorden.model.Orden;
import java.util.List;

public interface OrdenService {

    // Crear orden
    Orden crearOrden(CrearOrdenDTO dto);

    // Obtener por id
    Orden obtenerPorId(Long id);

    // Obtener órdenes de un comprador
    List<Orden> obtenerPorComprador(Long compradorId);

    // Obtener órdenes de un vendedor
    List<Orden> obtenerPorVendedor(Long vendedorId);

    // Obtener por estado
    List<Orden> obtenerPorEstado(String estado);

    // Actualizar estado de la orden
    Orden actualizarEstado(Long id, ActualizarOrdenDTO dto);

    // Cancelar orden
    Orden cancelarOrden(Long id);
}