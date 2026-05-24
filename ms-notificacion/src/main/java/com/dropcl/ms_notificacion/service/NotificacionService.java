package com.dropcl.ms_notificacion.service;

import com.dropcl.ms_notificacion.dto.NotificacionDto;
import com.dropcl.ms_notificacion.model.Notificacion;
import java.util.List;

public interface NotificacionService {

    Notificacion crear(NotificacionDto dto);

    List<Notificacion> obtenerPorUsuario(Long usuarioId);

    List<Notificacion> obtenerNoLeidas(Long usuarioId);

    Notificacion marcarComoLeida(Long id);

    void marcarTodasComoLeidas(Long usuarioId);

    long contarNoLeidas(Long usuarioId);

    void eliminar(Long id);
}