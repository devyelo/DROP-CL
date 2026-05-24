package com.dropcl.msusuario.service;

import com.dropcl.msusuario.dto.ActualizarPerfilDTO;
import com.dropcl.msusuario.dto.CrearDireccionDTO;
import com.dropcl.msusuario.dto.CrearPerfilDTO;
import com.dropcl.msusuario.model.DireccionEnvio;
import com.dropcl.msusuario.model.Perfil;
import java.util.List;

public interface PerfilService {


    Perfil crearPerfil(CrearPerfilDTO dto);


    Perfil obtenerPorId(Long id);


    Perfil obtenerPorUsuarioId(Long usuarioId);


    List<Perfil> obtenerTodos();

    
    Perfil actualizarPerfil(Long id, ActualizarPerfilDTO dto);


    DireccionEnvio agregarDireccion(Long perfilId, CrearDireccionDTO dto);


    List<DireccionEnvio> obtenerDirecciones(Long perfilId);


    void eliminarDireccion(Long direccionId);
}