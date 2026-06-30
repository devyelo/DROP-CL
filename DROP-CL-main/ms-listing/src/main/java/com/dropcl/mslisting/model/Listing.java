package com.dropcl.mslisting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al vendedor en ms-usuario
    @NotNull(message = "El vendedorId es obligatorio")
    @Column(name = "vendedor_id", nullable = false)
    private Long vendedorId;

    // Referencia al producto en ms-producto
    @NotNull(message = "El productoId es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    // Referencia a la talla en ms-producto
    @NotNull(message = "El tallaId es obligatorio")
    @Column(name = "talla_id", nullable = false)
    private Long tallaId;

    @NotBlank(message = "El valor de talla es obligatorio")
    @Column(name = "talla_valor", nullable = false)
    private String tallaValor;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVenta tipoVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoListing estado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CondicionProducto condicion;

    @Size(max = 500)
    private String descripcion;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaPublicacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.estado = EstadoListing.ACTIVO;
        // Por defecto expira en 30 días
        this.fechaExpiracion = LocalDateTime.now().plusDays(30);
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public enum TipoVenta {
        PRECIO_FIJO, SUBASTA
    }

    public enum EstadoListing {
        ACTIVO, PAUSADO, VENDIDO, EXPIRADO, CANCELADO
    }

    public enum CondicionProducto {
        NUEVO, COMO_NUEVO, USADO_BUEN_ESTADO, USADO
    }
}