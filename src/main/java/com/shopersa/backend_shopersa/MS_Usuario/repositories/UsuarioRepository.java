package com.shopersa.backend_shopersa.MS_Usuario.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    // Método que Spring Security usará para encontrar usuarios por su email (username)
    Optional<Usuario> findByEmail(String email);

    // Método para verificar si un email ya existe (útil para el registro)
    Boolean existsByEmail(String email);

    // Conteo de usuarios por nombre del permiso (rol)
    long countByPermiso_Nombre(String nombre);
}