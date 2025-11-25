package com.shopersa.backend_shopersa.MS_Admin.controllers;

import com.shopersa.backend_shopersa.MS_Admin.dto.AdminStatsDTO;
import com.shopersa.backend_shopersa.MS_Admin.dto.AdminStatsDTO.LowStockItem;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;
import com.shopersa.backend_shopersa.MS_Producto.repositories.ProductoRepository;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;
import com.shopersa.backend_shopersa.MS_Venta.repositories.VentaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin")
public class AdminStatsController {

    @Autowired private ProductoRepository productoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private VentaRepository ventaRepository;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminStatsDTO> getStats() {
        List<Producto> productos = (List<Producto>) productoRepository.findAll();
        long totalProductos = productos.size();
        
        long ofertas = productos.stream().filter(p -> Boolean.TRUE.equals(p.isEs_oferta())).count();
        long novedades = productos.stream().filter(p -> Boolean.TRUE.equals(p.isEs_novedad())).count();

        List<Producto> productosConStockBajo = productoRepository.findByEstadoProductoIsTrueAndStockLessThanEqual(5);
        
        List<LowStockItem> lowStock = productosConStockBajo.stream()
                .sorted(Comparator.comparingInt(Producto::getStock))
                .limit(5)
                .map(p -> new LowStockItem(p.getId(), p.getNombre(), p.getStock()))
                .collect(Collectors.toList());


        long totalClientes = usuarioRepository.countByPermiso_Nombre("Cliente");
        long totalVentas = ventaRepository.count();

        AdminStatsDTO dto = new AdminStatsDTO(totalProductos, totalClientes, totalVentas, ofertas, novedades, lowStock);
        return ResponseEntity.ok(dto);
    }
}