package com.dropcl.ms_notificacion.service.impl;

import com.dropcl.ms_notificacion.dto.NotificacionDto;
import com.dropcl.ms_notificacion.model.Notificacion;
import com.dropcl.ms_notificacion.repository.NotificacionRepository;
import com.dropcl.ms_notificacion.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    private final NotificacionRepository notificacionRepository;

    @Override
    public Notificacion crear(NotificacionDto dto) {
        log.info("Creando notificación tipo {} para usuario {}", dto.getTipo(), dto.getUsuarioId());

        Notificacion notificacion = Notificacion.builder()
                .usuarioId(dto.getUsuarioId())
                .tipo(Notificacion.TipoNotificacion.valueOf(dto.getTipo()))
                .mensaje(dto.getMensaje())
                .referenciaId(dto.getReferenciaId())
                .origen(dto.getOrigen())
                .build();

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Notificación creada con id: {}", guardada.getId());
        return guardada;
    }

    @Override
    public List<Notificacion> obtenerPorUsuario(Long usuarioId) {
        log.info("Obteniendo notificaciones del usuario: {}", usuarioId);
        return notificacionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Notificacion> obtenerNoLeidas(Long usuarioId) {
        log.info("Obteniendo notificaciones no leídas del usuario: {}", usuarioId);
        return notificacionRepository.findByUsuarioIdAndEstado(
                usuarioId, Notificacion.EstadoNotificacion.NO_LEIDA);
    }

    @Override
    public Notificacion marcarComoLeida(Long id) {
        log.info("Marcando notificación {} como leída", id);

        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notificación no encontrada con id: {}", id);
                    return new RuntimeException("Notificación no encontrada con id: " + id);
                });

        notificacion.setEstado(Notificacion.EstadoNotificacion.LEIDA);
        notificacion.setFechaLeida(LocalDateTime.now());

        Notificacion actualizada = notificacionRepository.save(notificacion);
        log.info("Notificación {} marcada como leída", id);
        return actualizada;
    }

    @Override
    public void marcarTodasComoLeidas(Long usuarioId) {
        log.info("Marcando todas las notificaciones del usuario {} como leídas", usuarioId);

        List<Notificacion> noLeidas = notificacionRepository.findByUsuarioIdAndEstado(
                usuarioId, Notificacion.EstadoNotificacion.NO_LEIDA);

        noLeidas.forEach(n -> {
            n.setEstado(Notificacion.EstadoNotificacion.LEIDA);
            n.setFechaLeida(LocalDateTime.now());
        });

        notificacionRepository.saveAll(noLeidas);
        log.info("{} notificaciones marcadas como leídas para usuario {}", noLeidas.size(), usuarioId);
    }

    @Override
    public long contarNoLeidas(Long usuarioId) {
        long cantidad = notificacionRepository.countByUsuarioIdAndEstado(
                usuarioId, Notificacion.EstadoNotificacion.NO_LEIDA);
        log.info("Usuario {} tiene {} notificaciones no leídas", usuarioId, cantidad);
        return cantidad;
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando notificación con id: {}", id);

        if (!notificacionRepository.existsById(id)) {
            log.warn("No se encontró notificación con id: {}", id);
            throw new RuntimeException("La notificación no encontrada con id: " + id);
        }

        notificacionRepository.deleteById(id);
        log.info("Notificación {} fue eliminada exitosamente", id);
    }
}