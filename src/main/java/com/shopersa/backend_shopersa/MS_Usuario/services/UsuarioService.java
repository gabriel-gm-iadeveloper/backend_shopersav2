package com.shopersa.backend_shopersa.MS_Usuario.services;

import java.util.List;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;

public interface UsuarioService {
    Usuario crear(Usuario usuario);
    Usuario obtenerId(Long id);
    List<Usuario> listarTodos();
    void eliminar(Long id);
    Usuario actualizar(Long id, Usuario usuarioActualizado);
    // Nuevo: actualizar solo el estado (habilitado/bloqueado)
    Usuario actualizarEstado(Long id, boolean estado);
}