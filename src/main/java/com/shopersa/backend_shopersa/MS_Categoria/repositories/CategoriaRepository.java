package com.shopersa.backend_shopersa.MS_Categoria.repositories;

import org.springframework.data.repository.CrudRepository;
import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;

public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
}