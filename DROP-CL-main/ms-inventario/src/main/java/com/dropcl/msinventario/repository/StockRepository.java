package com.dropcl.msinventario.repository;

import com.dropcl.msinventario.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByProductoId(Long productoId);

    Optional<Stock> findByProductoIdAndTallaId(Long productoId, Long tallaId);

    boolean existsByProductoIdAndTallaId(Long productoId, Long tallaId);

    List<Stock> findByEstado(Stock.EstadoStock estado);

    List<Stock> findByProductoIdAndEstado(Long productoId, Stock.EstadoStock estado);
}