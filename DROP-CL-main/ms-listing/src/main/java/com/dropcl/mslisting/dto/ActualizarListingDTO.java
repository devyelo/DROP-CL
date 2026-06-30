package com.dropcl.mslisting.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ActualizarListingDTO {

    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Size(max = 500)
    private String descripcion;

    @Pattern(regexp = "ACTIVO|PAUSADO|CANCELADO", message = "Estado inválido")
    private String estado;
}