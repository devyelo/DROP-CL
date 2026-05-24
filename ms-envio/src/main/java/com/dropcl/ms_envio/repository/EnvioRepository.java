package com.dropcl.ms_envio.repository;

import com.dropcl.ms_envio.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Optional<Envio> findByOrdenId(Long ordenId);

    Optional<Envio> findByNumeroSeguimiento(String numeroSeguimiento);
}