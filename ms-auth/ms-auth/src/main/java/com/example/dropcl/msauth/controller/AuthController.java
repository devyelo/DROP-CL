package com.example.dropcl.msauth.controller;

import com.example.dropcl.msauth.dto.LoginRequestDTO;
import com.example.dropcl.msauth.dto.RegisterRequestDTO;
import com.example.dropcl.msauth.model.Usuario;
import com.example.dropcl.msauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterRequestDTO dto) {
        log.info("Petición de registro recibida para: {}", dto.getEmail());
        try {
            Usuario usuario = authService.registrar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (RuntimeException e) {
            log.error("Error en registro: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        log.info("Petición de login recibida para: {}", dto.getEmail());
        try {
            Usuario usuario = authService.login(dto);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            log.error("Error en login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerActivos() {
        log.info("Petición para obtener usuarios activos");
        List<Usuario> usuarios = authService.obtenerActivos();
        return ResponseEntity.ok(usuarios);
    }


    @GetMapping("/usuarios/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Petición para obtener usuario con id: {}", id);
        try {
            Usuario usuario = authService.obtenerPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            log.error("Error al obtener usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        log.info("Petición para desactivar usuario con id: {}", id);
        try {
            authService.desactivarUsuario(id);
            return ResponseEntity.ok("Usuario desactivado correctamente");
        } catch (RuntimeException e) {
            log.error("Error al desactivar usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}