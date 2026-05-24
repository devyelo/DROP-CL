package com.dropcl.msinventario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AjusteStockDTO {

    @NotNull(message = "La cantidad de ajuste es obligatoria")
    private Integer cantidad; // puede ser positivo (entrada) o negativo (salida)

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;
}