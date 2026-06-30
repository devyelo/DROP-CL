package com.dropcl.ms_envio.service.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import com.dropcl.ms_envio.dto.EnvioDto;
import com.dropcl.ms_envio.model.Envio;
import com.dropcl.ms_envio.repository.EnvioRepository;

@ExtendWith(MockitoExtension.class)
class EnvioServiceImplTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private EnvioServiceImpl envioService;

    // --- PRUEBA 1: CAMINO FELIZ (Crear un envío exitosamente) ---
    @Test
    void debeCrearEnvio_CuandoOrdenNoTieneEnvioPrevio() {
        // GIVEN
        Long ordenId = 150L;
        EnvioDto dtoMock = mock(EnvioDto.class);
        when(dtoMock.getOrdenId()).thenReturn(ordenId);
        when(dtoMock.getTransportista()).thenReturn("Starken");
        when(dtoMock.getDireccionEnvio()).thenReturn("Santiago Centro");

        Envio envioGuardado = Envio.builder()
                .ordenId(ordenId)
                .transportista("Starken")
                .direccionEnvio("Santiago Centro")
                .build();

        // Caso 1 en caso que la BD dice "No encontré nada para esta orden"
        when(envioRepository.findByOrdenId(ordenId)).thenReturn(Optional.empty());
        // Simulamos que al guardar, devuelve el objeto
        when(envioRepository.save(any(Envio.class))).thenReturn(envioGuardado);

        // WHEN
        Envio resultado = envioService.crearEnvio(dtoMock);

        // THEN
        assertNotNull(resultado);
        assertEquals(ordenId, resultado.getOrdenId());

        verify(envioRepository, times(1)).findByOrdenId(ordenId);
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    // --- PRUEBA 2: CAMINO NEGATIVO (Regla de negocio: Evitar duplicados) ---
    @Test
    void debeLanzarExcepcion_CuandoSeIntentaDuplicarEnvio() {
        // GIVEN
        Long ordenId = 150L;
        EnvioDto dtoMock = mock(EnvioDto.class);
        when(dtoMock.getOrdenId()).thenReturn(ordenId);

        // Simulamos que la BD SÍ encuentra un envío para esa orden
        when(envioRepository.findByOrdenId(ordenId)).thenReturn(Optional.of(new Envio()));

        // WHEN & THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            envioService.crearEnvio(dtoMock);
        });

        assertEquals("Ya existe un envío registrado para la orden ID: " + ordenId, excepcion.getMessage());

        // Verificamos que se consultó, pero NUNCA llegó a guardar
        verify(envioRepository, times(1)).findByOrdenId(ordenId);
        verify(envioRepository, never()).save(any(Envio.class));
    }

    // --- PRUEBA 3: CAMINO FELIZ (Actualizar estado con éxito) ---
    @Test
    void debeActualizarEstado_CuandoEnvioExisteYEstadoEsValido() {
        // GIVEN
        Long envioId = 1L;
        String nuevoEstado = "EN_TRANSITO"; // Cambia este string por uno de tus Enum reales

        Envio envioExistente = Envio.builder().ordenId(10L).build();

        // El método actualizarEstado llama internamente a obtenerPorId
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioExistente));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioExistente);

        // WHEN
        Envio resultado = envioService.actualizarEstado(envioId, nuevoEstado);

        // THEN
        assertNotNull(resultado);
        verify(envioRepository, times(1)).findById(envioId);
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    // --- PRUEBA 4: CAMINO NEGATIVO (Excepción de estado inválido) ---
    @Test
    void debeLanzarExcepcion_CuandoEstadoEsInvalido() {
        // GIVEN
        Long envioId = 1L;
        String estadoInvalido = "ESTADO_INVENTADO";

        Envio envioExistente = new Envio();
        when(envioRepository.findById(envioId)).thenReturn(Optional.of(envioExistente));

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> {
            envioService.actualizarEstado(envioId, estadoInvalido);
        });

        // Verificamos que buscó en la BD pero no guardó nada erróneo
        verify(envioRepository, times(1)).findById(envioId);
        verify(envioRepository, never()).save(any(Envio.class));
    }
}