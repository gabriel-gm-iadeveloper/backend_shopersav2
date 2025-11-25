package com.shopersa.backend_shopersa.MS_Categoria.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;
import com.shopersa.backend_shopersa.MS_Categoria.services.CategoriaService;

@CrossOrigin(originPatterns = "http://localhost:*")
@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAll();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria, Authentication authentication) {
    
        if (authentication == null) {
            return new ResponseEntity<>("DEBUG: El objeto Authentication es NULO.", HttpStatus.UNAUTHORIZED);
        }

        System.out.println("--- DEBUG DENTRO DEL CONTROLADOR ---");
        System.out.println("Usuario autenticado: " + authentication.getName());
        authentication.getAuthorities().forEach(authority -> System.out.println("Permiso encontrado: " + authority.getAuthority()));
        System.out.println("------------------------------------");

        boolean tieneRolAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (tieneRolAdmin) {
            Categoria nuevaCategoria = categoriaService.save(categoria);
            return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
        } else {
            List<String> permisosEncontrados = authentication.getAuthorities().stream()
                                                .map(GrantedAuthority::getAuthority)
                                                .collect(Collectors.toList());
            return new ResponseEntity<>("DEBUG: El usuario NO tiene ROLE_ADMIN. Permisos encontrados: " + permisosEncontrados, HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoriaDetails) {
        Categoria categoriaActualizada = categoriaService.update(id, categoriaDetails);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        categoriaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}