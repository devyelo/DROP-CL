package com.dropcl.ms_oferta.service.impl;

import com.dropcl.ms_oferta.dto.OfertaDto;
import com.dropcl.ms_oferta.model.Oferta;
import com.dropcl.ms_oferta.repository.OfertaRepository;
import com.dropcl.ms_oferta.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfertaServiceImpl implements OfertaService {

    private static final Logger log = LoggerFactory.getLogger(OfertaServiceImpl.class);
    private final OfertaRepository ofertaRepository;

    @Override
    public Oferta crearOferta(OfertaDto dto) {
        log.info("Usuario {} creando oferta de {} para el producto {}", dto.getUsuarioId(), dto.getMonto(), dto.getProductoId());

        Oferta oferta = Oferta.builder()
                .usuarioId(dto.getUsuarioId())
                .productoId(dto.getProductoId())
                .monto(dto.getMonto())
                .build();

        return ofertaRepository.save(oferta);
    }

    @Override
    public Oferta obtenerPorId(Long id) {
        return ofertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferta no encontrada con ID: " + id));
    }

    @Override
    public List<Oferta> obtenerPorProducto(Long productoId) {
        log.info("Buscando ofertas para el producto ID: {}", productoId);
        return ofertaRepository.findByProductoId(productoId);
    }

    @Override
    public List<Oferta> obtenerPorUsuario(Long usuarioId) {
        log.info("Buscando ofertas del usuario ID: {}", usuarioId);
        return ofertaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public Oferta obtenerOfertaMasAlta(Long productoId) {
        log.info("Buscando la oferta mas alta para el producto ID: {}", productoId);
        List<Oferta> ofertasActivas = ofertaRepository.findByProductoIdAndEstadoOrderByMontoDesc(
                productoId, Oferta.EstadoOferta.PENDIENTE);

        if (ofertasActivas.isEmpty()) {
            throw new RuntimeException("No hay ofertas activas para el producto ID: " + productoId);
        }

        return ofertasActivas.get(0);
    }

    @Override
    public Oferta actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado de la oferta {} a {}", id, nuevoEstado);
        Oferta oferta = obtenerPorId(id);

        try {
            oferta.setEstado(Oferta.EstadoOferta.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de oferta no valido: " + nuevoEstado);
        }

        return ofertaRepository.save(oferta);
    }
}