package com.dropcl.ms_notificacion.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 500, message = "El mensaje no puede superar los 500 caracteres")
    private String mensaje;

    private Long referenciaId;

    private String origen;
}