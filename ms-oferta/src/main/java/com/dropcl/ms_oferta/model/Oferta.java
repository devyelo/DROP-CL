package com.dropcl.ms_oferta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ofertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId; // El comprador que hace la oferta

    @NotNull(message = "El ID del producto es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId; // Lo que quiere comprar

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    @Column(nullable = false)
    private BigDecimal monto;

    @NotNull(message = "El estado de la oferta es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOferta estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @NotNull(message = "La fecha de expiracion es obligatoria")
    @Future(message = "La fecha de expiracion debe estar en el futuro")
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoOferta.PENDIENTE;
        }

        if (this.fechaExpiracion == null) {
            this.fechaExpiracion = LocalDateTime.now().plusDays(30);
        }
    }

    public enum EstadoOferta {
        PENDIENTE,
        ACEPTADA,
        RECHAZADA,
        EXPIRADA,
        CANCELADA
    }
}