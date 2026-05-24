package com.dropcl.msusuario.repository;

import com.dropcl.msusuario.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);

    boolean existsByRut(String rut);
}
