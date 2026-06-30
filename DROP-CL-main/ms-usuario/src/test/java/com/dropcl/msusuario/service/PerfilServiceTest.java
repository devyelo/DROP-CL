package com.dropcl.msusuario.service;

import com.dropcl.msusuario.dto.ActualizarPerfilDTO;
import com.dropcl.msusuario.dto.CrearDireccionDTO;
import com.dropcl.msusuario.dto.CrearPerfilDTO;
import com.dropcl.msusuario.model.DireccionEnvio;
import com.dropcl.msusuario.model.Perfil;
import com.dropcl.msusuario.repository.DireccionEnvioRepository;
import com.dropcl.msusuario.repository.PerfilRepository;
import com.dropcl.msusuario.service.impl.PerfilServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private DireccionEnvioRepository direccionEnvioRepository;

    @InjectMocks
    private PerfilServiceImpl perfilService;

    private CrearPerfilDTO crearPerfilDTO;
    private Perfil perfil;

    @BeforeEach
    void setUp() {
        crearPerfilDTO = new CrearPerfilDTO();
        crearPerfilDTO.setUsuarioId(1L);
        crearPerfilDTO.setNombre("Juan");
        crearPerfilDTO.setApellido("Pérez");
        crearPerfilDTO.setRut("12345678-9");
        crearPerfilDTO.setTelefono("+56912345678");

        perfil = Perfil.builder()
                .id(1L)
                .usuarioId(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .rut("12345678-9")
                .telefono("+56912345678")
                .build();
    }



    @Test
    void crearPerfil_cuandoDatosValidos_debeCrearPerfil() {
        // GIVEN
        when(perfilRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(perfilRepository.existsByRut("12345678-9")).thenReturn(false);
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // WHEN
        Perfil resultado = perfilService.crearPerfil(crearPerfilDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellido());
        verify(perfilRepository, times(1)).save(any(Perfil.class));
    }

    @Test
    void crearPerfil_cuandoUsuarioYaTienePerfil_debeLanzarExcepcion() {
        // GIVEN
        when(perfilRepository.existsByUsuarioId(1L)).thenReturn(true);

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> perfilService.crearPerfil(crearPerfilDTO));

        assertEquals("Ya existe un perfil para este usuario", ex.getMessage());
        verify(perfilRepository, never()).save(any());
    }

    @Test
    void crearPerfil_cuandoRutYaRegistrado_debeLanzarExcepcion() {
        // GIVEN
        when(perfilRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(perfilRepository.existsByRut("12345678-9")).thenReturn(true);

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> perfilService.crearPerfil(crearPerfilDTO));

        assertEquals("El RUT ya está registrado", ex.getMessage());
        verify(perfilRepository, never()).save(any());
    }



    @Test
    void obtenerPorId_cuandoExiste_debeRetornarPerfil() {
        // GIVEN
        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));

        // WHEN
        Perfil resultado = perfilService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN
        when(perfilRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> perfilService.obtenerPorId(99L));

        assertEquals("Perfil no encontrado con id: 99", ex.getMessage());
    }



    @Test
    void actualizarPerfil_cuandoExiste_debeActualizarSoloCamposNoNulos() {
        // GIVEN
        ActualizarPerfilDTO dto = new ActualizarPerfilDTO();
        dto.setNombre("Carlos");
        dto.setApellido(null); // este no se debe tocar

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // WHEN
        Perfil resultado = perfilService.actualizarPerfil(1L, dto);

        // THEN
        assertEquals("Carlos", perfil.getNombre());
        assertEquals("Pérez", perfil.getApellido()); // no cambió
        verify(perfilRepository, times(1)).save(perfil);
    }



    @Test
    void agregarDireccion_cuandoPerfilExiste_debeGuardarDireccion() {
        // GIVEN
        CrearDireccionDTO dto = new CrearDireccionDTO();
        dto.setCalle("Av. Providencia");
        dto.setNumero("1234");
        dto.setComuna("Providencia");
        dto.setRegion("Metropolitana");
        dto.setCiudad("Santiago");
        dto.setEsPrincipal(true);

        DireccionEnvio direccion = DireccionEnvio.builder()
                .id(1L)
                .calle("Av. Providencia")
                .esPrincipal(true)
                .perfil(perfil)
                .build();

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));
        when(direccionEnvioRepository.save(any(DireccionEnvio.class))).thenReturn(direccion);

        // WHEN
        DireccionEnvio resultado = perfilService.agregarDireccion(1L, dto);

        // THEN
        assertNotNull(resultado);
        assertTrue(resultado.getEsPrincipal());
        assertEquals("Av. Providencia", resultado.getCalle());
        verify(direccionEnvioRepository, times(1)).save(any(DireccionEnvio.class));
    }

    

    @Test
    void eliminarDireccion_cuandoExiste_debeEliminarCorrectamente() {
        // GIVEN
        when(direccionEnvioRepository.existsById(1L)).thenReturn(true);

        // WHEN
        perfilService.eliminarDireccion(1L);

        // THEN
        verify(direccionEnvioRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarDireccion_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN
        when(direccionEnvioRepository.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> perfilService.eliminarDireccion(99L));

        assertEquals("Dirección no encontrada con id: 99", ex.getMessage());
        verify(direccionEnvioRepository, never()).deleteById(any());
    }
}