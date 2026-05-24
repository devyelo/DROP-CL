package com.dropcl.ms_notificacion.repository;

import com.dropcl.ms_notificacion.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Todas las notificaciones de un usuario
    List<Notificacion> findByUsuarioId(Long usuarioId);

    // Solo las no leídas de un usuario
    List<Notificacion> findByUsuarioIdAndEstado(Long usuarioId, Notificacion.EstadoNotificacion estado);

    // Por tipo de notificación
    List<Notificacion> findByTipo(Notificacion.TipoNotificacion tipo);

    // Contar no leídas de un usuario
    long countByUsuarioIdAndEstado(Long usuarioId, Notificacion.EstadoNotificacion estado);
}