package com.shopersa.backend_shopersa.MS_Permisos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Permisos.services.PermisoServices;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/permisos")
public class PermisoRestController {

    @Autowired
    PermisoServices permisoServices;

    @PostMapping
    public ResponseEntity<Permiso> crearPermiso(@RequestBody Permiso permiso) {
        Permiso nuevaPermiso = permisoServices.crear(permiso);
        return ResponseEntity.ok(nuevaPermiso);
    }

     @GetMapping("/{id}")
    public ResponseEntity<Permiso> obtenerPermisoPorId(@PathVariable Long id) {
        Permiso permiso = permisoServices.obtenerId(id);
        return ResponseEntity.ok(permiso);
    }

 
    @GetMapping
    public ResponseEntity<List<Permiso>> listarPermisos() {
        List<Permiso> permisos = permisoServices.listarTodas();
        return ResponseEntity.ok(permisos);
    }

  
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable Long id) {
        permisoServices.eliminar(id);
        return ResponseEntity.noContent().build();
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Permiso> actualizarPermiso(@PathVariable Long id, @RequestBody Permiso permisoActualizada) {
        Permiso permiso = permisoServices.actualizar(id, permisoActualizada);
        return ResponseEntity.ok(permiso);
    }

}
