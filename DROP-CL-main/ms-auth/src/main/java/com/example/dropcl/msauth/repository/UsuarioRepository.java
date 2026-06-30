package com.example.dropcl.msauth.repository;

import com.example.dropcl.msauth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    Optional<Usuario> findByEmail(String email);


    boolean existsByEmail(String email);


    java.util.List<Usuario> findByRol(Usuario.RolUsuario rol);


    java.util.List<Usuario> findByActivoTrue();
}
