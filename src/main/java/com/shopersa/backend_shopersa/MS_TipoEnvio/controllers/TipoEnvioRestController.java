package com.shopersa.backend_shopersa.MS_TipoEnvio.controllers;

import com.shopersa.backend_shopersa.MS_TipoEnvio.entities.TipoEnvio;
import com.shopersa.backend_shopersa.MS_TipoEnvio.services.TipoEnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-envio")
@CrossOrigin(origins = "http://localhost:5173")
public class TipoEnvioRestController {

    @Autowired
    private TipoEnvioService tipoEnvioService;

    // Cualquiera puede ver las opciones de envío disponibles
    @GetMapping
    public ResponseEntity<List<TipoEnvio>> getAll() {
        return ResponseEntity.ok(tipoEnvioService.findAll());
    }

    // Solo un admin puede crear un nuevo tipo de envío
    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<TipoEnvio> create(@RequestBody TipoEnvio tipoEnvio) {
        return ResponseEntity.ok(tipoEnvioService.save(tipoEnvio));
    }

    // Solo un admin puede eliminar un tipo de envío
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoEnvioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}