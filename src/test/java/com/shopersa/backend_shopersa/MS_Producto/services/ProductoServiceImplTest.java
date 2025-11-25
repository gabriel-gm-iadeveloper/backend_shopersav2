// src/test/java/com/shopersa/backend_shopersa/MS_Producto/services/ProductoServiceImplTest.java
package com.shopersa.backend_shopersa.MS_Producto.services;

import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;
import com.shopersa.backend_shopersa.MS_Categoria.repositories.CategoriaRepository;
import com.shopersa.backend_shopersa.MS_Producto.dto.ProductoDTO;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;
import com.shopersa.backend_shopersa.MS_Producto.repositories.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @InjectMocks private ProductoServiceImpl service;
    @Mock private ProductoRepository productoRepository;
    @Mock private CategoriaRepository categoriaRepository;

    @Test
    void guardar_ok_mapeaDTOyCategoria() {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Zapatillas");
        dto.setDescripcion("Running");
        dto.setPrecio(49990.0);
        dto.setStock(10);
        dto.setCategoriaId(1L);
        dto.setEstadoProducto(true);

        Categoria cat = new Categoria();
        cat.setId(1L);
        cat.setNombre("Calzado");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> {
            Producto p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        Producto out = service.save(dto);

        assertNotNull(out.getId());
        assertEquals("Zapatillas", out.getNombre());
        assertEquals(1L, out.getCategoria().getId());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void ajustarStock_ok_sumaYResta() {
        Producto p = new Producto();
        p.setId(5L);
        p.setStock(5);
        when(productoRepository.findById(5L)).thenReturn(Optional.of(p));
        when(productoRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // resta 3 → queda 2
        Producto out = service.ajustarStock(5L, -3);

        assertEquals(2, out.getStock());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void ajustarStock_error_noPermiteNegativo() {
        Producto p = new Producto();
        p.setId(7L);
        p.setStock(2);
        when(productoRepository.findById(7L)).thenReturn(Optional.of(p));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.ajustarStock(7L, -5));
        assertTrue(ex.getMessage().toLowerCase().contains("stock"));
        verify(productoRepository, never()).save(any());
    }

    @Test
    void addImages_respetaTopeDeTres() {
        Producto p = new Producto();
        p.setId(9L);
        // Inicializa con una imagen existente usando ProductImage
        com.shopersa.backend_shopersa.MS_Producto.entities.ProductImage img = new com.shopersa.backend_shopersa.MS_Producto.entities.ProductImage();
        img.setProducto(p);
        img.setUrl("a.jpg");
        img.setOrden(0);
        p.setImagenes(new java.util.ArrayList<>(java.util.List.of(img))); // ya tiene 1
        when(productoRepository.findById(9L)).thenReturn(java.util.Optional.of(p));
        when(productoRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(inv -> inv.getArgument(0));

        Producto out = service.addImages(9L, java.util.List.of("b.jpg", "c.jpg", "d.jpg", "e.jpg"));

        org.junit.jupiter.api.Assertions.assertEquals(3, out.getImagenes().size()); // tope 3
        org.junit.jupiter.api.Assertions.assertTrue(out.getImagenes().stream().anyMatch(pi -> "a.jpg".equals(pi.getUrl())));
        org.junit.jupiter.api.Assertions.assertTrue(out.getImagenes().stream().anyMatch(pi -> "b.jpg".equals(pi.getUrl()) || "c.jpg".equals(pi.getUrl()) || "d.jpg".equals(pi.getUrl())));
    }
}
