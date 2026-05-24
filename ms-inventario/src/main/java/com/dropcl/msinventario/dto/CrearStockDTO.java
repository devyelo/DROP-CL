package com.dropcl.msinventario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearStockDTO {

    @NotNull(message = "El productoId es obligatorio")
    private Long productoId;

    @NotNull(message = "El tallaId es obligatorio")
    private Long tallaId;

    @NotBlank(message = "El valor de talla es obligatorio")
    private String tallaValor;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}