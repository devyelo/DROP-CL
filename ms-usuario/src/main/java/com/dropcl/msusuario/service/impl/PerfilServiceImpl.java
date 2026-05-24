package com.dropcl.msusuario.service.impl;

import com.dropcl.msusuario.dto.ActualizarPerfilDTO;
import com.dropcl.msusuario.dto.CrearDireccionDTO;
import com.dropcl.msusuario.dto.CrearPerfilDTO;
import com.dropcl.msusuario.model.DireccionEnvio;
import com.dropcl.msusuario.model.Perfil;
import com.dropcl.msusuario.repository.DireccionEnvioRepository;
import com.dropcl.msusuario.repository.PerfilRepository;
import com.dropcl.msusuario.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private static final Logger log = LoggerFactory.getLogger(PerfilServiceImpl.class);

    private final PerfilRepository perfilRepository;
    private final DireccionEnvioRepository direccionEnvioRepository;

    @Override
    public Perfil crearPerfil(CrearPerfilDTO dto) {
        log.info("Creando perfil para usuarioId: {}", dto.getUsuarioId());


        if (perfilRepository.existsByUsuarioId(dto.getUsuarioId())) {
            log.warn("Ya existe un perfil para usuarioId: {}", dto.getUsuarioId());
            throw new RuntimeException("Ya existe un perfil para este usuario");
        }


        if (dto.getRut() != null && perfilRepository.existsByRut(dto.getRut())) {
            log.warn("RUT ya registrado: {}", dto.getRut());
            throw new RuntimeException("El RUT ya está registrado");
        }

        Perfil perfil = Perfil.builder()
                .usuarioId(dto.getUsuarioId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .rut(dto.getRut())
                .telefono(dto.getTelefono())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();

        Perfil guardado = perfilRepository.save(perfil);
        log.info("Perfil creado exitosamente con id: {}", guardado.getId());
        return guardado;
    }

    @Override
    public Perfil obtenerPorId(Long id) {
        log.info("Buscando perfil con id: {}", id);
        return perfilRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Perfil no encontrado con id: {}", id);
                    return new RuntimeException("Perfil no encontrado con id: " + id);
                });
    }

    @Override
    public Perfil obtenerPorUsuarioId(Long usuarioId) {
        log.info("Buscando perfil para usuarioId: {}", usuarioId);
        return perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.warn("Perfil no encontrado para usuarioId: {}", usuarioId);
                    return new RuntimeException("Perfil no encontrado para usuarioId: " + usuarioId);
                });
    }

    @Override
    public List<Perfil> obtenerTodos() {
        log.info("Obteniendo todos los perfiles");
        return perfilRepository.findAll();
    }

    @Override
    public Perfil actualizarPerfil(Long id, ActualizarPerfilDTO dto) {
        log.info("Actualizando perfil con id: {}", id);

        Perfil perfil = obtenerPorId(id);


        if (dto.getNombre() != null) perfil.setNombre(dto.getNombre());
        if (dto.getApellido() != null) perfil.setApellido(dto.getApellido());
        if (dto.getTelefono() != null) perfil.setTelefono(dto.getTelefono());
        if (dto.getFechaNacimiento() != null) perfil.setFechaNacimiento(dto.getFechaNacimiento());

        Perfil actualizado = perfilRepository.save(perfil);
        log.info("Perfil actualizado exitosamente con id: {}", actualizado.getId());
        return actualizado;
    }

    @Override
    public DireccionEnvio agregarDireccion(Long perfilId, CrearDireccionDTO dto) {
        log.info("Agregando dirección al perfil con id: {}", perfilId);

        Perfil perfil = obtenerPorId(perfilId);

        DireccionEnvio direccion = DireccionEnvio.builder()
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .departamento(dto.getDepartamento())
                .comuna(dto.getComuna())
                .region(dto.getRegion())
                .ciudad(dto.getCiudad())
                .codigoPostal(dto.getCodigoPostal())
                .esPrincipal(dto.getEsPrincipal() != null ? dto.getEsPrincipal() : false)
                .perfil(perfil)
                .build();

        DireccionEnvio guardada = direccionEnvioRepository.save(direccion);
        log.info("Dirección agregada con id: {}", guardada.getId());
        return guardada;
    }

    @Override
    public List<DireccionEnvio> obtenerDirecciones(Long perfilId) {
        log.info("Obteniendo direcciones del perfil con id: {}", perfilId);
        return direccionEnvioRepository.findByPerfilId(perfilId);
    }

    @Override
    public void eliminarDireccion(Long direccionId) {
        log.info("Eliminando dirección con id: {}", direccionId);
        if (!direccionEnvioRepository.existsById(direccionId)) {
            log.warn("Dirección no encontrada con id: {}", direccionId);
            throw new RuntimeException("Dirección no encontrada con id: " + direccionId);
        }
        direccionEnvioRepository.deleteById(direccionId);
        log.info("Dirección eliminada exitosamente con id: {}", direccionId);
    }
}