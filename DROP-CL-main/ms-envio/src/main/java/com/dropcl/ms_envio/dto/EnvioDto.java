package com.dropcl.ms_envio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioDto {

    @NotNull(message = "El ID de la orden es obligatorio")
    private Long ordenId;

    @NotBlank(message = "El transportista es obligatorio")
    private String transportista;

    @NotBlank(message = "La direccion del envio debe ser obligatoria")
    private String direccionEnvio;
}