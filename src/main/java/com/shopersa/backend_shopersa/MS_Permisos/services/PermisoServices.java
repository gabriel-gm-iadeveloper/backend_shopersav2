package com.shopersa.backend_shopersa.MS_Permisos.services;

import java.util.List;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;

public interface PermisoServices {

    Permiso crear(Permiso permiso);
    Permiso obtenerId(Long id);
    List<Permiso> listarTodas();    
    void eliminar(Long id);
    Permiso actualizar(Long id, Permiso permisoActualizado);

}
