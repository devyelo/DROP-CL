package com.dropcl.ms_pago.service;

import com.dropcl.ms_pago.dto.PagoDto;
import com.dropcl.ms_pago.model.Pago;

import java.util.List;

public interface PagoService {
    Pago crearPago(PagoDto dto);
    Pago obtenerPorId(Long id);
    List<Pago> obtenerPorOrdenId(Long ordenId);
    Pago actualizarEstado(Long id, String nuevoEstado, String codigoTransaccion);
}