package com.dropcl.msusuario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class CrearPerfilDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100)
    private String apellido;

    @Pattern(regexp = "^[0-9]{8,9}-[0-9kK]$", message = "RUT inválido")
    private String rut;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Teléfono inválido")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @Data
    public static class CrearDireccionDTO {

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
}
