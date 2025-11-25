package com.shopersa.backend_shopersa.MS_Categoria.services;

import java.util.List;
import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;

public interface CategoriaService {
    List<Categoria> findAll();
    Categoria findById(Long id);
    Categoria save(Categoria categoria);
    void deleteById(Long id);
    Categoria update(Long id, Categoria categoria);
}