package com.dropcl.ms_notificacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario debe ser obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacion tipo;

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    @Column(nullable = false)
    private String mensaje;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoNotificacion estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_leida")
    private LocalDateTime fechaLeida;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(name = "origen")
    private String origen;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoNotificacion.NO_LEIDA;
    }

    public enum TipoNotificacion {
        ORDEN_CREADA,
        PAGO_CONFIRMADO,
        PAGO_RECHAZADO,
        ENVIO_EN_CAMINO,
        ENVIO_ENTREGADO,
        OFERTA_SUPERADA,
        LISTING_VENDIDO
    }

    public enum EstadoNotificacion {
        NO_LEIDA,
        LEIDA
    }
}