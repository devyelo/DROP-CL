package com.dropcl.msorden.repository;

import com.dropcl.msorden.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

    List<Orden> findByCompradorId(Long compradorId);

    List<Orden> findByVendedorId(Long vendedorId);

    List<Orden> findByEstado(Orden.EstadoOrden estado);

    Optional<Orden> findByListingId(Long listingId);

    boolean existsByListingId(Long listingId);

    List<Orden> findByCompradorIdAndEstado(Long compradorId, Orden.EstadoOrden estado);
}