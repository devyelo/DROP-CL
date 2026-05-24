package com.example.dropcl.msauth.service.impl;

import com.example.dropcl.msauth.dto.LoginRequestDTO;
import com.example.dropcl.msauth.dto.RegisterRequestDTO;
import com.example.dropcl.msauth.model.Usuario;
import com.example.dropcl.msauth.repository.UsuarioRepository;
import com.example.dropcl.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Logger para trazabilidad (SLF4J)
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    // Inyección por constructor (gracias a @RequiredArgsConstructor de Lombok)
    private final UsuarioRepository usuarioRepository;

    @Override
    public Usuario registrar(RegisterRequestDTO dto) {
        log.info("Intentando registrar usuario con email: {}", dto.getEmail());

        // Regla de negocio: no permitir emails duplicados
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            log.warn("Email ya registrado: {}", dto.getEmail());
            throw new RuntimeException("El email ya está registrado");
        }

        // Construir entidad desde el DTO
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(Usuario.RolUsuario.valueOf(dto.getRol().toUpperCase()))
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con id: {}", guardado.getId());
        return guardado;
    }

    @Override
    public Usuario login(LoginRequestDTO dto) {
        log.info("Intento de login para email: {}", dto.getEmail());

        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login fallido - email no encontrado: {}", dto.getEmail());
                    return new RuntimeException("Credenciales incorrectas");
                });

        // Regla de negocio: usuario debe estar activo
        if (!usuario.getActivo()) {
            log.warn("Login fallido - usuario inactivo: {}", dto.getEmail());
            throw new RuntimeException("Usuario inactivo");
        }

        // Verificar contraseña (simple por ahora, sin encriptación)
        if (!usuario.getPassword().equals(dto.getPassword())) {
            log.warn("Login fallido - contraseña incorrecta para: {}", dto.getEmail());
            throw new RuntimeException("Credenciales incorrectas");
        }

        log.info("Login exitoso para usuario id: {}", usuario.getId());
        return usuario;
    }

    @Override
    public List<Usuario> obtenerActivos() {
        log.info("Obteniendo lista de usuarios activos");
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    public void desactivarUsuario(Long id) {
        log.info("Desactivando usuario con id: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });

        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        log.info("Usuario con id: {} desactivado exitosamente", id);
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado con id: {}", id);
                    return new RuntimeException("Usuario no encontrado con id: " + id);
                });
    }
}