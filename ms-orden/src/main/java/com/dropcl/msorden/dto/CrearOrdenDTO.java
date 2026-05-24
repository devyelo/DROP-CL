package com.dropcl.msorden.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearOrdenDTO {

    @NotNull(message = "El compradorId es obligatorio")
    private Long compradorId;

    @NotNull(message = "El listingId es obligatorio")
    private Long listingId;

    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionEnvio;

    @Size(max = 500)
    private String notas;
}