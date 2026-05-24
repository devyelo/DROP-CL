package com.example.dropcl.msauth.repository;

import com.example.dropcl.msauth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por email (para login)
    Optional<Usuario> findByEmail(String email);

    // Verificar si ya existe un email registrado
    boolean existsByEmail(String email);

    // Buscar usuarios por rol
    java.util.List<Usuario> findByRol(Usuario.RolUsuario rol);

    // Buscar solo usuarios activos
    java.util.List<Usuario> findByActivoTrue();
}
