package com.shopersa.backend_shopersa.MS_Producto.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopersa.backend_shopersa.MS_Auth.Security.JwtAuthenticationFilter;
import com.shopersa.backend_shopersa.MS_Producto.dto.ProductoDTO;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;
import com.shopersa.backend_shopersa.MS_Producto.services.ImageStorageService;
import com.shopersa.backend_shopersa.MS_Producto.services.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductoRestController.class)
@AutoConfigureMockMvc(addFilters = false) // no aplicar filtros de seguridad en este slice
class ProductoRestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    // Dependencias del controller
    @MockBean private ProductoService productoService;
    @MockBean private ImageStorageService imageStorage; // <- el controller lo inyecta por ctor

    // Mocks para que la config de seguridad no rompa el contexto
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void verProductos_ok() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(new Producto()));

        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void verUnProducto_ok() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        when(productoService.findById(1L)).thenReturn(p);

        mockMvc.perform(get("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void productoNoExiste_404() throws Exception {
        when(productoService.findById(99L))
                .thenThrow(new RuntimeException("Producto no encontrado"));

        mockMvc.perform(get("/api/productos/99")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearProducto_201() throws Exception {
        ProductoDTO dto = new ProductoDTO();
        dto.setNombre("Zapatillas");
        dto.setDescripcion("Running");
        dto.setPrecio(49990.0);
        dto.setStock(15);
        dto.setCategoriaId(1L);
        dto.setEstadoProducto(true);
        dto.setImagenes(List.of("http://img/1.jpg"));

        Producto guardado = new Producto();
        guardado.setId(100L);
        when(productoService.save(any(ProductoDTO.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void desactivar_200() throws Exception {
        Producto p = new Producto();
        p.setId(1L);
        p.setEstadoProducto(false);
        when(productoService.desactivar(1L)).thenReturn(p);

        mockMvc.perform(patch("/api/productos/1/desactivar"))
            .andExpect(status().isOk());
    }
}
