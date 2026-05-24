package com.dropcl.msorden.service;

import com.dropcl.msorden.dto.*;
import com.dropcl.msorden.model.Orden;
import java.util.List;

public interface OrdenService {

    
    Orden crearOrden(CrearOrdenDTO dto);


    Orden obtenerPorId(Long id);


    List<Orden> obtenerPorComprador(Long compradorId);


    List<Orden> obtenerPorVendedor(Long vendedorId);


    List<Orden> obtenerPorEstado(String estado);


    Orden actualizarEstado(Long id, ActualizarOrdenDTO dto);


    Orden cancelarOrden(Long id);
}