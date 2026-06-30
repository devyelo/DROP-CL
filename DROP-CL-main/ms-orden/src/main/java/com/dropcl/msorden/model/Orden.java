package com.dropcl.msorden.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "El compradorId es obligatorio")
    @Column(name = "comprador_id", nullable = false)
    private Long compradorId;


    @NotNull(message = "El vendedorId es obligatorio")
    @Column(name = "vendedor_id", nullable = false)
    private Long vendedorId;


    @NotNull(message = "El listingId es obligatorio")
    @Column(name = "listing_id", nullable = false, unique = true)
    private Long listingId;


    @NotNull(message = "El productoId es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;


    @NotNull(message = "El tallaId es obligatorio")
    @Column(name = "talla_id", nullable = false)
    private Long tallaId;

    @NotBlank(message = "El valor de talla es obligatorio")
    @Column(name = "talla_valor", nullable = false)
    private String tallaValor;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoOrden estado;

    @Column(name = "direccion_envio", nullable = false)
    private String direccionEnvio;

    @Column(name = "fecha_orden", nullable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_entrega_estimada")
    private LocalDateTime fechaEntregaEstimada;

    @Size(max = 500)
    private String notas;

    @PrePersist
    public void prePersist() {
        this.fechaOrden = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoOrden.PENDIENTE;
        // Estimado de entrega 7 días
        this.fechaEntregaEstimada = LocalDateTime.now().plusDays(7);
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoOrden {
        PENDIENTE, PAGADA, EN_PREPARACION, ENVIADA, ENTREGADA, CANCELADA
    }
}