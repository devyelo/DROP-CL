package com.dropcl.mslisting.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CrearListingDTO {

    @NotNull(message = "El vendedorId es obligatorio")
    private Long vendedorId;

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "El tallaId es obligatorio")
    private Long tallaId;

    @NotBlank(message = "El valor de talla es obligatorio")
    private String tallaValor;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @NotBlank(message = "El tipo de venta es obligatorio")
    @Pattern(regexp = "PRECIO_FIJO|SUBASTA", message = "Tipo de venta inválido")
    private String tipoVenta;

    @NotBlank(message = "La condición es obligatoria")
    @Pattern(regexp = "NUEVO|COMO_NUEVO|USADO_BUEN_ESTADO|USADO", message = "Condición inválida")
    private String condicion;

    @Size(max = 500)
    private String descripcion;
}