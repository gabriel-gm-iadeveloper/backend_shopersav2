package com.shopersa.backend_shopersa.MS_Producto.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.shopersa.backend_shopersa.MS_Producto.dto.ProductoDTO;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;
import com.shopersa.backend_shopersa.MS_Producto.services.ImageStorageService;
import com.shopersa.backend_shopersa.MS_Producto.services.ProductoService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    private final ProductoService productoService;
    private final ImageStorageService imageStorage;

    public ProductoRestController(ProductoService productoService, ImageStorageService imageStorage) {
        this.productoService = productoService;
        this.imageStorage = imageStorage;
    }

    //---- Públicos ----
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        return ResponseEntity.ok(productoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.findById(id));
    }

    //---- ADMIN ----
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> createProducto(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto nuevo = productoService.save(productoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @Valid @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.update(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.desactivar(id));
    }

    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> activar(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.activar(id));
    }

    //---- Stock ----
    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> ajustarStock(@PathVariable Long id, @RequestParam("delta") int delta) {
        return ResponseEntity.ok(productoService.ajustarStock(id, delta));
    }

    //---- Imágenes ----
    @PostMapping("/{id}/imagenes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> subirImagenes(@PathVariable Long id,
                                                  @RequestParam("files") List<MultipartFile> files) throws IOException {
        List<String> urls = new ArrayList<>();
        for (MultipartFile f : files) {
            if (urls.size() >= 3) break; 
            urls.add(imageStorage.saveForProduct(f, id));
        }
        return ResponseEntity.ok(productoService.addImages(id, urls));
    }

    @DeleteMapping("/{id}/imagenes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> eliminarImagen(@PathVariable Long id,
                                                   @RequestParam("url") String url) throws IOException {
        imageStorage.deleteByUrl(url); 
        return ResponseEntity.ok(productoService.removeImage(id, url));
    }

    @PatchMapping("/{id}/imagenes/reordenar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> reordenarImagenes(@PathVariable Long id,
                                                      @RequestBody List<Long> nuevasIds) {
        return ResponseEntity.ok(productoService.reorderImages(id, nuevasIds));
    }
}
