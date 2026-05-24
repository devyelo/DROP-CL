package com.dropcl.mslisting.repository;

import com.dropcl.mslisting.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {

    List<Listing> findByVendedorId(Long vendedorId);

    List<Listing> findByProductoId(Long productoId);

    List<Listing> findByEstado(Listing.EstadoListing estado);

    List<Listing> findByTipoVenta(Listing.TipoVenta tipoVenta);

    List<Listing> findByVendedorIdAndEstado(Long vendedorId, Listing.EstadoListing estado);

    List<Listing> findByProductoIdAndEstado(Long productoId, Listing.EstadoListing estado);

    boolean existsByVendedorIdAndProductoIdAndTallaIdAndEstado(
            Long vendedorId, Long productoId, Long tallaId, Listing.EstadoListing estado);
}