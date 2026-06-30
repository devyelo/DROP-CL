package com.example.dropcl.msauth.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
