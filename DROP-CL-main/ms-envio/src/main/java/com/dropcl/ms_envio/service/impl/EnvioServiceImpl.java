package com.dropcl.ms_envio.service.impl;

import com.dropcl.ms_envio.dto.EnvioDto;
import com.dropcl.ms_envio.model.Envio;
import com.dropcl.ms_envio.repository.EnvioRepository;
import com.dropcl.ms_envio.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvioServiceImpl implements EnvioService {

    private static final Logger log = LoggerFactory.getLogger(EnvioServiceImpl.class);
    private final EnvioRepository envioRepository;

    @Override
    public Envio crearEnvio(EnvioDto dto) {
        log.info("Creando envío para la orden: {}", dto.getOrdenId());

        // Regla de negocio: Validar si ya existe un envío para esta orden
        if (envioRepository.findByOrdenId(dto.getOrdenId()).isPresent()) {
            log.warn("Intento de duplicar envío para orden ID: {}", dto.getOrdenId());
            throw new RuntimeException("Ya existe un envío registrado para la orden ID: " + dto.getOrdenId());
        }

        Envio envio = Envio.builder()
                .ordenId(dto.getOrdenId())
                .transportista(dto.getTransportista())
                .direccionEnvio(dto.getDireccionEnvio())
                .build();

        return envioRepository.save(envio);
    }

    @Override
    public Envio obtenerPorId(Long id) {
        return envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));
    }

    @Override
    public Envio obtenerPorOrdenId(Long ordenId) {
        return envioRepository.findByOrdenId(ordenId)
                .orElseThrow(() -> new RuntimeException("No se encontró envío para la orden ID: " + ordenId));
    }

    @Override
    public List<Envio> obtenerTodos() {
        return envioRepository.findAll();
    }

    @Override
    public Envio actualizarEstado(Long id, String nuevoEstado) {
        log.info("Actualizando estado del envío {} a {}", id, nuevoEstado);
        Envio envio = obtenerPorId(id);

        try {
            envio.setEstado(Envio.EstadoEnvio.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de envío no válido: " + nuevoEstado);
        }

        return envioRepository.save(envio);
    }

    @Override
    public Envio asignarSeguimiento(Long id, String numeroSeguimiento) {
        log.info("Asignando número de seguimiento {} al envío {}", numeroSeguimiento, id);
        Envio envio = obtenerPorId(id);
        envio.setNumeroSeguimiento(numeroSeguimiento);
        return envioRepository.save(envio);
    }
}