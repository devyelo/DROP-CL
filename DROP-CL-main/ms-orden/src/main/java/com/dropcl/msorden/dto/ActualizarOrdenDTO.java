package com.dropcl.msorden.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ActualizarOrdenDTO {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "PAGADA|EN_PREPARACION|ENVIADA|ENTREGADA|CANCELADA",
            message = "Estado inválido")
    private String estado;

    @Size(max = 500)
    private String notas;
}