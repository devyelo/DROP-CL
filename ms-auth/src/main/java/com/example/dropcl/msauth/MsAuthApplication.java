package com.dropcl.msusuario.service.impl;

import com.dropcl.msusuario.model.DireccionEnvio;
import com.dropcl.msusuario.repository.DireccionEnvioRepository;
import com.dropcl.msusuario.service.DireccionEnvioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DireccionEnvioServiceImpl implements DireccionEnvioService {

	private static final Logger log = LoggerFactory.getLogger(DireccionEnvioServiceImpl.class);
	private final DireccionEnvioRepository direccionRepo;

	@Override
	@Transactional
	public DireccionEnvio crearDireccion(DireccionEnvio direccion) {
		log.info("Creando dirección para perfil id: {}", direccion.getPerfil().getId());

		// Si es la primera dirección, se marca como principal automáticamente
		if (!direccionRepo.existsByPerfilIdAndEsPrincipalTrue(direccion.getPerfil().getId())) {
			direccion.setEsPrincipal(true);
			log.info("Primera dirección del perfil, marcada como principal automáticamente");
		}

		DireccionEnvio guardada = direccionRepo.save(direccion);
		log.info("Dirección creada con id: {}", guardada.getId());
		return guardada;
	}

	@Override
	public List<DireccionEnvio> listarPorPerfil(Long perfilId) {
		log.info("Listando direcciones del perfil id: {}", perfilId);
		return direccionRepo.findByPerfilId(perfilId);
	}

	@Override
	public DireccionEnvio obtenerPrincipal(Long perfilId) {
		log.info("Obteniendo dirección principal del perfil id: {}", perfilId);
		return direccionRepo.findByPerfilIdAndEsPrincipalTrue(perfilId)
				.orElseThrow(() -> {
					log.warn("No se encontró dirección principal para perfil id: {}", perfilId);
					return new RuntimeException("No hay dirección principal para este perfil");
				});
	}

	@Override
	@Transactional
	public DireccionEnvio actualizarDireccion(Long id, DireccionEnvio direccion) {
		log.info("Actualizando dirección id: {}", id);
		DireccionEnvio existente = direccionRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Dirección no encontrada con id: " + id));

		existente.setCalle(direccion.getCalle());
		existente.setNumero(direccion.getNumero());
		existente.setDepartamento(direccion.getDepartamento());
		existente.setComuna(direccion.getComuna());
		existente.setRegion(direccion.getRegion());
		existente.setCiudad(direccion.getCiudad());
		existente.setCodigoPostal(direccion.getCodigoPostal());

		DireccionEnvio actualizada = direccionRepo.save(existente);
		log.info("Dirección actualizada correctamente, id: {}", actualizada.getId());
		return actualizada;
	}

	@Override
	@Transactional
	public void eliminarDireccion(Long id) {
		log.info("Eliminando dirección id: {}", id);
		DireccionEnvio existente = direccionRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Dirección no encontrada con id: " + id));

		if (existente.getEsPrincipal()) {
			log.warn("Intento de eliminar dirección principal id: {}", id);
			throw new RuntimeException("No puedes eliminar la dirección principal");
		}

		direccionRepo.deleteById(id);
		log.info("Dirección eliminada correctamente, id: {}", id);
	}

	@Override
	@Transactional
	public DireccionEnvio marcarComoPrincipal(Long id, Long perfilId) {
		log.info("Marcando como principal la dirección id: {} del perfil id: {}", id, perfilId);

		// Quitar principal a la que la tiene
		direccionRepo.findByPerfilIdAndEsPrincipalTrue(perfilId)
				.ifPresent(actual -> {
					actual.setEsPrincipal(false);
					direccionRepo.save(actual);
					log.info("Dirección id: {} dejó de ser principal", actual.getId());
				});

		// Marcar la nueva como principal
		DireccionEnvio nueva = direccionRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Dirección no encontrada con id: " + id));

		nueva.setEsPrincipal(true);
		DireccionEnvio resultado = direccionRepo.save(nueva);
		log.info("Dirección id: {} es ahora la principal", resultado.getId());
		return resultado;
	}
}