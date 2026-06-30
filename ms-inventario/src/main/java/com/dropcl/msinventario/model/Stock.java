package com.dropcl.msinventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al producto en ms-producto (no FK real, es entre microservicios)
    @NotNull(message = "El productoId es obligatorio")
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    // Referencia a la talla en ms-producto
    @NotNull(message = "El tallaId es obligatorio")
    @Column(name = "talla_id", nullable = false)
    private Long tallaId;

    @NotBlank(message = "El valor de la talla es obligatorio")
    @Column(name = "talla_valor", nullable = false)
    private String tallaValor;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidad;

    @Min(value = 0, message = "La cantidad mínima no puede ser negativa")
    @Column(name = "cantidad_minima", nullable = false)
    private Integer cantidadMinima;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoStock estado;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.cantidadMinima = 1;
        this.estado = EstadoStock.DISPONIBLE;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        // Regla de negocio: actualizar estado según cantidad
        if (this.cantidad == 0) {
            this.estado = EstadoStock.AGOTADO;
        } else if (this.cantidad <= this.cantidadMinima) {
            this.estado = EstadoStock.BAJO_STOCK;
        } else {
            this.estado = EstadoStock.DISPONIBLE;
        }
    }

    public enum EstadoStock {
        DISPONIBLE, BAJO_STOCK, AGOTADO
    }
}