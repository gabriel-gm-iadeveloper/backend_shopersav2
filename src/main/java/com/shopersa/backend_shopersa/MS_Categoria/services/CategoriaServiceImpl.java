package com.shopersa.backend_shopersa.MS_Categoria.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;
import com.shopersa.backend_shopersa.MS_Categoria.repositories.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> findAll() {
        return (List<Categoria>) categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    @Override
    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void deleteById(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    public Categoria update(Long id, Categoria categoriaDetails) {
        Categoria categoria = findById(id);
        categoria.setNombre(categoriaDetails.getNombre());
        return categoriaRepository.save(categoria);
    }
}