package com.shopersa.backend_shopersa.MS_Usuario.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;


@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
// --------- SPRING SECURITY ----------
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("No se encontró el usuario con email: " + email));

        String roleName = (usuario.getPermiso() != null && usuario.getPermiso().getNombre() != null)
                ? usuario.getPermiso().getNombre().toUpperCase()
                : "CLIENTE";

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);

        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(Collections.singletonList(authority))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.isEstado()) // si estado=false => deshabilitado
                .build();
    }

    // MÉTODOS CRUD QUE YA TENÍAS
    @Override
    public Usuario crear(Usuario usuario) {
        // La creación real con encriptación la haremos en el AuthController
        return usuarioRepository.save(usuario);
    }
    
    @Override
    public Usuario obtenerId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Override
    public List<Usuario> listarTodos() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Usuario existente = obtenerId(id);
        existente.setNombres(usuarioActualizado.getNombres());
        existente.setApellidos(usuarioActualizado.getApellidos());
        existente.setEmail(usuarioActualizado.getEmail());
        existente.setFecha_nacimiento(usuarioActualizado.getFecha_nacimiento());
        existente.setTelefono(usuarioActualizado.getTelefono());
        existente.setRut(usuarioActualizado.getRut());
        existente.setDireccion_calle(usuarioActualizado.getDireccion_calle());
        existente.setDireccion_numero(usuarioActualizado.getDireccion_numero());
        existente.setComuna(usuarioActualizado.getComuna());
        existente.setRegion(usuarioActualizado.getRegion());
        existente.setEstado(usuarioActualizado.isEstado());
        if (usuarioActualizado.getPermiso() != null) {
            existente.setPermiso(usuarioActualizado.getPermiso());
        }
        // Si el password viene en el payload (ya encriptado desde el controlador), actualizarlo
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            existente.setPassword(usuarioActualizado.getPassword());
        }
        return usuarioRepository.save(existente);
    }

    @Override
    public Usuario actualizarEstado(Long id, boolean estado) {
        Usuario existente = obtenerId(id);
        existente.setEstado(estado);
        return usuarioRepository.save(existente);
    }
}