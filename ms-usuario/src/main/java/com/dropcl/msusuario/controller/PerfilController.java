package com.dropcl.msusuario.controller;

import com.dropcl.msusuario.dto.ActualizarPerfilDTO;
import com.dropcl.msusuario.dto.CrearDireccionDTO;
import com.dropcl.msusuario.dto.CrearPerfilDTO;
import com.dropcl.msusuario.model.DireccionEnvio;
import com.dropcl.msusuario.model.Perfil;
import com.dropcl.msusuario.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/perfiles")
@RequiredArgsConstructor
public class PerfilController {

    private static final Logger log = LoggerFactory.getLogger(PerfilController.class);

    private final PerfilService perfilService;


    @PostMapping
    public ResponseEntity<?> crearPerfil(@Valid @RequestBody CrearPerfilDTO dto) {
        log.info("Petición para crear perfil para usuarioId: {}", dto.getUsuarioId());
        try {
            Perfil perfil = perfilService.crearPerfil(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(perfil);
        } catch (RuntimeException e) {
            log.error("Error al crear perfil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<List<Perfil>> obtenerTodos() {
        log.info("Petición para obtener todos los perfiles");
        return ResponseEntity.ok(perfilService.obtenerTodos());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        log.info("Petición para obtener perfil con id: {}", id);
        try {
            return ResponseEntity.ok(perfilService.obtenerPorId(id));
        } catch (RuntimeException e) {
            log.error("Error al obtener perfil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        log.info("Petición para obtener perfil de usuarioId: {}", usuarioId);
        try {
            return ResponseEntity.ok(perfilService.obtenerPorUsuarioId(usuarioId));
        } catch (RuntimeException e) {
            log.error("Error al obtener perfil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPerfil(@PathVariable Long id,
                                              @Valid @RequestBody ActualizarPerfilDTO dto) {
        log.info("Petición para actualizar perfil con id: {}", id);
        try {
            return ResponseEntity.ok(perfilService.actualizarPerfil(id, dto));
        } catch (RuntimeException e) {
            log.error("Error al actualizar perfil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/{perfilId}/direcciones")
    public ResponseEntity<?> agregarDireccion(@PathVariable Long perfilId,
                                              @Valid @RequestBody CrearDireccionDTO dto) {
        log.info("Petición para agregar dirección al perfil con id: {}", perfilId);
        try {
            DireccionEnvio direccion = perfilService.agregarDireccion(perfilId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(direccion);
        } catch (RuntimeException e) {
            log.error("Error al agregar dirección: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/{perfilId}/direcciones")
    public ResponseEntity<List<DireccionEnvio>> obtenerDirecciones(@PathVariable Long perfilId) {
        log.info("Petición para obtener direcciones del perfil con id: {}", perfilId);
        return ResponseEntity.ok(perfilService.obtenerDirecciones(perfilId));
    }


    @DeleteMapping("/direcciones/{direccionId}")
    public ResponseEntity<?> eliminarDireccion(@PathVariable Long direccionId) {
        log.info("Petición para eliminar dirección con id: {}", direccionId);
        try {
            perfilService.eliminarDireccion(direccionId);
            return ResponseEntity.ok("Dirección eliminada correctamente");
        } catch (RuntimeException e) {
            log.error("Error al eliminar dirección: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}