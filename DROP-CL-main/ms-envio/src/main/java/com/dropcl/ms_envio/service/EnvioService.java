package com.dropcl.ms_envio.service;

import com.dropcl.ms_envio.dto.EnvioDto;
import com.dropcl.ms_envio.model.Envio;
import java.util.List;

public interface EnvioService {
    Envio crearEnvio(EnvioDto dto);
    Envio obtenerPorId(Long id);
    Envio obtenerPorOrdenId(Long ordenId);
    List<Envio> obtenerTodos();
    Envio actualizarEstado(Long id, String nuevoEstado);
    Envio asignarSeguimiento(Long id, String numeroSeguimiento);
}