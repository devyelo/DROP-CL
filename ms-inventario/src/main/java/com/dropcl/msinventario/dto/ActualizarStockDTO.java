package com.dropcl.msinventario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ActualizarStockDTO {

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidad;
}