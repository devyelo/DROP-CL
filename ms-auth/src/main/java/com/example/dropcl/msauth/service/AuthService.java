package com.example.dropcl.msauth.service;

import com.example.dropcl.msauth.dto.LoginRequestDTO;
import com.example.dropcl.msauth.dto.RegisterRequestDTO;
import com.example.dropcl.msauth.model.Usuario;
import java.util.List;

public interface AuthService {

    // Registrar un nuevo usuario
    Usuario registrar(RegisterRequestDTO dto);

    // Login — retorna el usuario si las credenciales son correctas
    Usuario login(LoginRequestDTO dto);

    // Obtener todos los usuarios activos
    List<Usuario> obtenerActivos();

    // Desactivar un usuario (soft delete)
    void desactivarUsuario(Long id);

    // Obtener usuario por id
    Usuario obtenerPorId(Long id);
}