package com.shopersa.backend_shopersa.MS_Permisos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Permisos.repositories.PermisoRepository;

@Service
public class PermisoServicesImpl implements PermisoServices {

    @Autowired
    PermisoRepository permisoRepositories;


    @Override
    public Permiso actualizar(Long id, Permiso permisoActualizado) {
        Permiso existente = obtenerId(id);
        existente.setNombre(permisoActualizado.getNombre());
        existente.setDescripcion(permisoActualizado.getDescripcion());
        return permisoRepositories.save(existente);
    }

    @Override
    public Permiso crear(Permiso permiso) {
        return permisoRepositories.save(permiso);
    }

    @Override
    public void eliminar(Long id) {
        if (!permisoRepositories.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        permisoRepositories.deleteById(id);
    }

    @Override
    public List<Permiso> listarTodas() {
        return (List<Permiso>) permisoRepositories.findAll();
    }

    @Override
    public Permiso obtenerId(Long id) {
        return permisoRepositories.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
    }

    

}
