// src/main/java/com/shopersa/backend_shopersa/MS_Venta/controllers/VentaRestController.java
package com.shopersa.backend_shopersa.MS_Venta.controllers;

import com.shopersa.backend_shopersa.MS_Venta.dto.VentaAdminResponseDTO;
import com.shopersa.backend_shopersa.MS_Venta.dto.VentaDTO;
import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;
import com.shopersa.backend_shopersa.MS_Venta.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/ventas")
public class VentaRestController {

    @Autowired
    private VentaService ventaService;

    // Endpoint para que un cliente cree una nueva venta (realice una compra)
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Venta> crearNuevaVenta(@RequestBody VentaDTO ventaDTO, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Venta nuevaVenta = ventaService.crearVenta(ventaDTO, emailUsuario);
        return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
    }

    // Endpoint para que un cliente vea su propio historial de compras
    @GetMapping("/mis-compras")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<Venta>> getMisVentas(Authentication authentication) {
        String emailUsuario = authentication.getName();
        List<Venta> ventas = ventaService.findVentasByUsuarioEmail(emailUsuario);
        return ResponseEntity.ok(ventas);
    }

    // Endpoint para que un administrador vea todas las ventas del sistema
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaAdminResponseDTO>> getAllVentas() { // <-- Cambiar tipo de retorno
        List<VentaAdminResponseDTO> ventas = ventaService.findAllVentas();
        return ResponseEntity.ok(ventas);
    }
}