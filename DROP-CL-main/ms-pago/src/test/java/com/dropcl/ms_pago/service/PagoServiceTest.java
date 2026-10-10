package com.dropcl.ms_pago.service;

import com.dropcl.ms_pago.dto.PagoDto;
import com.dropcl.ms_pago.model.Pago;
import com.dropcl.ms_pago.repository.PagoRepository;
import com.dropcl.ms_pago.service.impl.PagoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoServiceImpl pagoService;

    private Pago pago;
    private PagoDto pagoDto;

    @BeforeEach
    void setUp() {
        pagoDto = new PagoDto();
        pagoDto.setOrdenId(1L);
        pagoDto.setMonto(new BigDecimal("50000"));
        pagoDto.setMetodoPago("WEBPAY");

        pago = Pago.builder()
                .id(1L)
                .ordenId(1L)
                .monto(new BigDecimal("50000"))
                .metodoPago(Pago.MetodoPago.WEBPAY)
                .estado(Pago.EstadoPago.PENDIENTE)
                .build();
    }

    

    @Test
    void crearPago_cuandoDatosValidos_debeGuardarPago() {
        // GIVEN
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        Pago resultado = pagoService.crearPago(pagoDto);

        // THEN
        assertNotNull(resultado);
        assertEquals(Pago.MetodoPago.WEBPAY, resultado.getMetodoPago());
        assertEquals(new BigDecimal("50000"), resultado.getMonto());
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void crearPago_cuandoMetodoPagoInvalido_debeLanzarExcepcion() {
        // GIVEN
        pagoDto.setMetodoPago("BITCOIN"); // no existe en el enum

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagoService.crearPago(pagoDto));

        assertEquals("Metodo de pago no valido: BITCOIN", ex.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void crearPago_cuandoMetodoPagoEnMinusculas_debeAceptarlo() {
        // GIVEN — prueba que el toUpperCase() funciona
        pagoDto.setMetodoPago("webpay");
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        Pago resultado = pagoService.crearPago(pagoDto);

        // THEN
        assertNotNull(resultado);
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }



    @Test
    void obtenerPorId_cuandoExiste_debeRetornarPago() {
        // GIVEN
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        // WHEN
        Pago resultado = pagoService.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(Pago.EstadoPago.PENDIENTE, resultado.getEstado());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_debeLanzarExcepcion() {
        // GIVEN
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagoService.obtenerPorId(99L));

        assertEquals("Pago no encontrado con ID: 99", ex.getMessage());
    }



    @Test
    void obtenerPorOrdenId_debeRetornarListaDePagos() {
        // GIVEN
        when(pagoRepository.findByOrdenId(1L)).thenReturn(List.of(pago));

        // WHEN
        List<Pago> resultado = pagoService.obtenerPorOrdenId(1L);

        // THEN
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getOrdenId());
    }



    @Test
    void actualizarEstado_cuandoEstadoValido_debeActualizarCorrectamente() {
        // GIVEN
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        Pago resultado = pagoService.actualizarEstado(1L, "COMPLETADO", "TBK-123456");

        // THEN
        assertEquals(Pago.EstadoPago.COMPLETADO, resultado.getEstado());
        assertEquals("TBK-123456", resultado.getCodigoTransaccion());
        verify(pagoRepository, times(1)).save(pago);
    }

    @Test
    void actualizarEstado_cuandoEstadoInvalido_debeLanzarExcepcion() {
        // GIVEN
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        // WHEN & THEN
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pagoService.actualizarEstado(1L, "ANULADO", null));

        assertEquals("Estado de pago no valido: ANULADO", ex.getMessage());
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void actualizarEstado_cuandoCodigoTransaccionBlanco_noDebeAsignarlo() {
        // GIVEN
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pago);

        // WHEN
        Pago resultado = pagoService.actualizarEstado(1L, "RECHAZADO", "   ");

        // THEN
        assertEquals(Pago.EstadoPago.RECHAZADO, resultado.getEstado());
        assertNull(resultado.getCodigoTransaccion()); // no se asignó porque era blank
        verify(pagoRepository, times(1)).save(pago);
    }
}