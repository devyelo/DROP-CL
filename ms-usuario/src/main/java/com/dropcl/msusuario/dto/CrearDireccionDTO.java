package com.dropcl.msusuario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CrearDireccionDTO {

    @NotBlank(message = "La calle es obligatoria")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    private String numero;

    private String departamento;

    @NotBlank(message = "La comuna es obligatoria")
    private String comuna;

    @NotBlank(message = "La región es obligatoria")
    private String region;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @Pattern(regexp = "^[0-9]{7}$", message = "Código postal debe tener 7 dígitos")
    private String codigoPostal;

    private Boolean esPrincipal;
}
