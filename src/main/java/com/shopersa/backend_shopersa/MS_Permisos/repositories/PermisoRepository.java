package com.shopersa.backend_shopersa.MS_Permisos.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;

public interface PermisoRepository extends CrudRepository<Permiso, Long> {

     Optional<Permiso> findByNombre(String nombre);

}
