package com.shopersa.backend_shopersa.MS_Envio.controllers;

import com.shopersa.backend_shopersa.MS_Envio.dto.EnvioDTO;
import com.shopersa.backend_shopersa.MS_Envio.entities.Envio;
import com.shopersa.backend_shopersa.MS_Envio.services.EnvioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/envios")
@CrossOrigin(origins = "http://localhost:5173")
public class EnvioRestController {

    @Autowired
    private EnvioService envioService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')") 
    public ResponseEntity<Envio> crear(@RequestBody EnvioDTO envioDTO, Authentication auth) {
        return ResponseEntity.ok(envioService.crearEnvio(envioDTO, auth.getName()));
    }
    
    @GetMapping("/mis-envios")
    @PreAuthorize("hasRole('CLIENTE')") 
    public ResponseEntity<List<Envio>> getMisEnvios(Authentication auth) {
        return ResponseEntity.ok(envioService.findByUsuarioEmail(auth.getName()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<List<Envio>> getAll() {
        return ResponseEntity.ok(envioService.findAll());
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')") 
    public ResponseEntity<Envio> updateEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String nuevoEstado = body.get("estado");
        if (nuevoEstado == null || nuevoEstado.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(envioService.updateEstado(id, nuevoEstado));
    }
}