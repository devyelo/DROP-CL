package com.dropcl.ms_envio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la orden es obligatorio")
    @Column(name = "orden_id", nullable = false, unique = true)
    private Long ordenId;

    @NotBlank(message = "El transportista es obligatorio")
    @Column(nullable = false)
    private String transportista; // ej: Chilexpress, Starken, BlueExpress

    @Column(name = "numero_seguimiento", unique = true)
    private String numeroSeguimiento;

    @NotBlank(message = "La dirección de envio debe ser obligatoria")
    @Column(name = "direccion_envio", nullable = false)
    private String direccionEnvio;

    @NotNull(message = "El estado del envio es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEnvio estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoEnvio.PREPARANDO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoEnvio {
        PREPARANDO,
        EN_TRANSITO,
        ENTREGADO,
        DEVUELTO
    }
}