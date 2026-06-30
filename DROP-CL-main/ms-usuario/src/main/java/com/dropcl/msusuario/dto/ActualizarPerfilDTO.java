package com.dropcl.msusuario.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ActualizarPerfilDTO {

    @Size(min = 2, max = 100)
    private String nombre;

    @Size(min = 2, max = 100)
    private String apellido;

    @Pattern(regexp = "^\\+?[0-9]{8,15}$", message = "Teléfono inválido")
    private String telefono;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;
}