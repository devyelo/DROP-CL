package com.dropcl.msinventario.service.impl;

import com.dropcl.msinventario.client.ProductoClient;
import com.dropcl.msinventario.dto.*;
import com.dropcl.msinventario.model.Stock;
import com.dropcl.msinventario.repository.StockRepository;
import com.dropcl.msinventario.service.StockService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private static final Logger log = LoggerFactory.getLogger(StockServiceImpl.class);

    private final StockRepository stockRepository;
    private final ProductoClient productoClient;

    @Override
    public Stock crearStock(CrearStockDTO dto) {
        log.info("Creando stock para productoId: {} tallaId: {}", dto.getProductoId(), dto.getTallaId());

        // Regla de negocio: verificar que el producto existe en ms-producto
        if (!productoClient.productoExiste(dto.getProductoId())) {
            log.warn("Producto id: {} no existe en ms-producto", dto.getProductoId());
            throw new RuntimeException("El producto no existe en el sistema");
        }

        // Regla de negocio: no puede haber stock duplicado para el mismo producto y talla
        if (stockRepository.existsByProductoIdAndTallaId(dto.getProductoId(), dto.getTallaId())) {
            log.warn("Ya existe stock para productoId: {} tallaId: {}", dto.getProductoId(), dto.getTallaId());
            throw new RuntimeException("Ya existe un registro de stock para este producto y talla");
        }

        Stock stock = Stock.builder()
                .productoId(dto.getProductoId())
                .tallaId(dto.getTallaId())
                .tallaValor(dto.getTallaValor())
                .cantidad(dto.getCantidad())
                .build();

        Stock guardado = stockRepository.save(stock);
        log.info("Stock creado con id: {}", guardado.getId());
        return guardado;
    }

    @Override
    public Stock obtenerPorId(Long id) {
        log.info("Buscando stock con id: {}", id);
        return stockRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Stock no encontrado con id: {}", id);
                    return new RuntimeException("Stock no encontrado con id: " + id);
                });
    }

    @Override
    public List<Stock> obtenerPorProducto(Long productoId) {
        log.info("Obteniendo stock del producto id: {}", productoId);
        return stockRepository.findByProductoId(productoId);
    }

    @Override
    public Stock obtenerPorProductoYTalla(Long productoId, Long tallaId) {
        log.info("Buscando stock para productoId: {} tallaId: {}", productoId, tallaId);
        return stockRepository.findByProductoIdAndTallaId(productoId, tallaId)
                .orElseThrow(() -> {
                    log.warn("Stock no encontrado para productoId: {} tallaId: {}", productoId, tallaId);
                    return new RuntimeException("Stock no encontrado para este producto y talla");
                });
    }

    @Override
    public Stock actualizarStock(Long id, ActualizarStockDTO dto) {
        log.info("Actualizando stock id: {} a cantidad: {}", id, dto.getCantidad());

        Stock stock = obtenerPorId(id);
        stock.setCantidad(dto.getCantidad());

        Stock actualizado = stockRepository.save(stock);
        log.info("Stock id: {} actualizado a cantidad: {}", id, actualizado.getCantidad());
        return actualizado;
    }

    @Override
    public Stock ajustarStock(Long id, AjusteStockDTO dto) {
        log.info("Ajustando stock id: {} en {} unidades. Motivo: {}", id, dto.getCantidad(), dto.getMotivo());

        Stock stock = obtenerPorId(id);
        int nuevaCantidad = stock.getCantidad() + dto.getCantidad();

        // Regla de negocio: el stock no puede quedar negativo
        if (nuevaCantidad < 0) {
            log.warn("Ajuste rechazado — stock insuficiente. Stock actual: {} Ajuste: {}",
                    stock.getCantidad(), dto.getCantidad());
            throw new RuntimeException("Stock insuficiente. Stock actual: " + stock.getCantidad());
        }

        stock.setCantidad(nuevaCantidad);
        Stock actualizado = stockRepository.save(stock);
        log.info("Stock id: {} ajustado. Nueva cantidad: {}", id, actualizado.getCantidad());
        return actualizado;
    }

    @Override
    public List<Stock> obtenerPorEstado(Stock.EstadoStock estado) {
        log.info("Obteniendo stock con estado: {}", estado);
        return stockRepository.findByEstado(estado);
    }

    @Override
    public boolean verificarDisponibilidad(Long productoId, Long tallaId, Integer cantidad) {
        log.info("Verificando disponibilidad — productoId: {} tallaId: {} cantidad: {}",
                productoId, tallaId, cantidad);

        return stockRepository.findByProductoIdAndTallaId(productoId, tallaId)
                .map(stock -> {
                    boolean disponible = stock.getCantidad() >= cantidad
                            && stock.getEstado() != Stock.EstadoStock.AGOTADO;
                    log.info("Disponibilidad para productoId: {} tallaId: {} → {}", productoId, tallaId, disponible);
                    return disponible;
                })
                .orElse(false);
    }
}