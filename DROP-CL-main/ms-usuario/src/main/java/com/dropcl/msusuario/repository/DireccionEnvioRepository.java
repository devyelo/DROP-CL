package com.dropcl.msusuario.repository;

import com.dropcl.msusuario.model.DireccionEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DireccionEnvioRepository extends JpaRepository<DireccionEnvio, Long> {

    List<DireccionEnvio> findByPerfilId(Long perfilId);

    List<DireccionEnvio> findByPerfilIdAndEsPrincipalTrue(Long perfilId);
}
