package com.dropcl.ms_pago.repository;

import com.dropcl.ms_pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByOrdenId(Long ordenId);

    Optional<Pago> findByCodigoTransaccion(String codigoTransaccion);
}