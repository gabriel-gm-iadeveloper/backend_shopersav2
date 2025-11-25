package com.shopersa.backend_shopersa.MS_TipoEnvio.repositories;

import com.shopersa.backend_shopersa.MS_TipoEnvio.entities.TipoEnvio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface TipoEnvioRepository extends CrudRepository<TipoEnvio, Long> {
    Optional<TipoEnvio> findByNombre(String nombre);
}