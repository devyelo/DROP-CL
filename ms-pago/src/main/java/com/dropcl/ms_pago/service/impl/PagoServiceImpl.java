package com.dropcl.ms_pago.service.impl;

import com.dropcl.ms_pago.dto.PagoDto;
import com.dropcl.ms_pago.model.Pago;
import com.dropcl.ms_pago.repository.PagoRepository;
import com.dropcl.ms_pago.service.PagoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);
    private final PagoRepository pagoRepository;

    @Override
    public Pago crearPago(PagoDto dto) {
        log.info("Creando intento de pago para la orden ID: {}", dto.getOrdenId());

        Pago pago = Pago.builder()
                .ordenId(dto.getOrdenId())
                .monto(dto.getMonto())
                .build();

        try {
            pago.setMetodoPago(Pago.MetodoPago.valueOf(dto.getMetodoPago().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Metodo de pago no valido: " + dto.getMetodoPago());
        }

        return pagoRepository.save(pago);
    }

    @Override
    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
    }

    @Override
    public List<Pago> obtenerPorOrdenId(Long ordenId) {
        log.info("Buscando pagos para la orden ID: {}", ordenId);
        return pagoRepository.findByOrdenId(ordenId);
    }

    @Override
    public Pago actualizarEstado(Long id, String nuevoEstado, String codigoTransaccion) {
        log.info("El estado del pago se esta actualizando {} a {}", id, nuevoEstado);
        Pago pago = obtenerPorId(id);

        try {
            pago.setEstado(Pago.EstadoPago.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de pago no valido: " + nuevoEstado);
        }

        if (codigoTransaccion != null && !codigoTransaccion.isBlank()) {
            pago.setCodigoTransaccion(codigoTransaccion);
        }

        return pagoRepository.save(pago);
    }
}