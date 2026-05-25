package com.dropcl.ms_oferta.repository;

import com.dropcl.ms_oferta.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    List<Oferta> findByUsuarioId(Long usuarioId);

    List<Oferta> findByProductoId(Long productoId);

    List<Oferta> findByProductoIdAndEstadoOrderByMontoDesc(Long productoId, Oferta.EstadoOferta estado);
}