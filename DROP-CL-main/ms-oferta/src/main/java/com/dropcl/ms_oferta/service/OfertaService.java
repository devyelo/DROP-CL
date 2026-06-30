package com.dropcl.ms_oferta.service;

import com.dropcl.ms_oferta.dto.OfertaDto;
import com.dropcl.ms_oferta.model.Oferta;

import java.util.List;

public interface OfertaService {
    Oferta crearOferta(OfertaDto dto);
    Oferta obtenerPorId(Long id);
    List<Oferta> obtenerPorProducto(Long productoId);
    List<Oferta> obtenerPorUsuario(Long usuarioId);
    Oferta obtenerOfertaMasAlta(Long productoId);
    Oferta actualizarEstado(Long id, String nuevoEstado);
}