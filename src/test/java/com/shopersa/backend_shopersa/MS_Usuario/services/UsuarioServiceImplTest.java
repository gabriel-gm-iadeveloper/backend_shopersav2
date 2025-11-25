// src/test/java/com/shopersa/backend_shopersa/MS_Usuario/services/UsuarioServiceImplTest.java
package com.shopersa.backend_shopersa.MS_Usuario.services;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @InjectMocks private UsuarioServiceImpl service;
    @Mock private UsuarioRepository usuarioRepository;

    @Test
    void loadUserByUsername_ok_conRol() {
        Permiso per = new Permiso();
        per.setNombre("Admin");

        Usuario u = new Usuario();
        u.setId(1L);
        u.setEmail("admin@shopersa.cl");
        u.setPassword("{noop}123456");
        u.setPermiso(per);
        u.setEstado(true);

        when(usuarioRepository.findByEmail("admin@shopersa.cl")).thenReturn(Optional.of(u));

        UserDetails ud = service.loadUserByUsername("admin@shopersa.cl");

        assertEquals("admin@shopersa.cl", ud.getUsername());
        assertTrue(ud.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(ud.isEnabled());
    }

    @Test
    void loadUserByUsername_noExiste_lanzaExcepcion() {
        when(usuarioRepository.findByEmail("x@y.cl")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("x@y.cl"));
    }

    @Test
    void actualizar_noPisaPasswordSiVieneNull() {
        Usuario existente = new Usuario();
        existente.setId(5L);
        existente.setEmail("a@b.cl");
        existente.setPassword("hash-seguro");
        existente.setEstado(true);

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usuario cambios = new Usuario();
        cambios.setEmail("nuevo@b.cl");
        cambios.setPassword(null); 
        cambios.setNombres("Nuevo");
        cambios.setApellidos("Nombre");
        cambios.setFecha_nacimiento(LocalDate.of(2000,1,1));
        cambios.setEstado(true);

        Usuario out = service.actualizar(5L, cambios);

        assertEquals("hash-seguro", out.getPassword());
        assertEquals("nuevo@b.cl", out.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }
}
