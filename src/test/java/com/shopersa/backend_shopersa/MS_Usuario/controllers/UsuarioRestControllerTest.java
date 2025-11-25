// src/test/java/com/shopersa/backend_shopersa/MS_Usuario/controllers/UsuarioRestControllerTest.java
package com.shopersa.backend_shopersa.MS_Usuario.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopersa.backend_shopersa.MS_Auth.Security.JwtAuthenticationFilter;
import com.shopersa.backend_shopersa.MS_Permisos.repositories.PermisoRepository; // IMPORTAR
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORTAR
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioRestController.class)
@AutoConfigureMockMvc(addFilters = false) // Mantenemos esto para simplificar
class UsuarioRestControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockBean private UsuarioService usuarioService;

  // --- CORRECCIÓN: Añadir los Mocks que faltaban ---
  @MockBean private PasswordEncoder passwordEncoder;
  @MockBean private PermisoRepository permisoRepository;
  // --------------------------------------------------

  @MockBean private UserDetailsService userDetailsService;
  @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter; 

  @Test
  @WithMockUser(roles = "ADMIN")
  void listarUsuarios_ok() throws Exception {
    when(usuarioService.listarTodos()).thenReturn(List.of(new Usuario()));
    mockMvc.perform(get("/api/usuarios").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void obtenerUsuario_ok() throws Exception {
    Usuario u = new Usuario();
    u.setId(1L);
    u.setEmail("admin@shopersa.cl");
    u.setNombres("Admin");
    when(usuarioService.obtenerId(1L)).thenReturn(u);

    mockMvc.perform(get("/api/usuarios/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void obtenerUsuario_noExiste_404() throws Exception {

    when(usuarioService.obtenerId(99L)).thenThrow(new RuntimeException("Usuario no encontrado"));
    mockMvc.perform(get("/api/usuarios/99").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound()); 
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void actualizarUsuario_ok() throws Exception {
    // 1. Preparar datos de entrada (DTO)
    com.shopersa.backend_shopersa.MS_Usuario.dto.AdminUpdateUserRequest cambios = new com.shopersa.backend_shopersa.MS_Usuario.dto.AdminUpdateUserRequest();
    cambios.setNombres("Nuevo Nombre");
    cambios.setEmail("nuevo@shopersa.cl");
    cambios.setPermiso("ADMIN");

    // 2. Simular la búsqueda del permiso
    com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso permisoAdmin = new com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso();
    permisoAdmin.setNombre("Admin");
    when(permisoRepository.findByNombre("Admin")).thenReturn(Optional.of(permisoAdmin));
    
    // 3. Simular la respuesta del servicio de actualización
    Usuario actualizado = new Usuario();
    actualizado.setId(1L);
    actualizado.setNombres("Nuevo Nombre");
    actualizado.setEmail("nuevo@shopersa.cl");
    
    when(usuarioService.actualizar(any(Long.class), any(Usuario.class))).thenReturn(actualizado);

    // 4. Ejecutar la llamada
    mockMvc.perform(put("/api/usuarios/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(cambios)))
        .andExpect(status().isOk());
  }
}