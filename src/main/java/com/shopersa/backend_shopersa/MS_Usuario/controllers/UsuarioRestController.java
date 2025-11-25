package com.shopersa.backend_shopersa.MS_Usuario.controllers;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Permisos.repositories.PermisoRepository;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.services.UsuarioService;
import com.shopersa.backend_shopersa.MS_Usuario.dto.AdminCreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(originPatterns = "http://localhost:*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PermisoRepository permisoRepository;

    // Solo un Admin puede ver la lista de todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // Solo un Admin puede ver un usuario por su ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerId(id));
    }

    // Solo un Admin puede actualizar la información de cualquier usuario
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody com.shopersa.backend_shopersa.MS_Usuario.dto.AdminUpdateUserRequest req) {
        // Resolver permiso desde string
        Permiso permiso = null;
        if (req.getPermiso() != null && !req.getPermiso().isBlank()) {
            String lookup = req.getPermiso().equalsIgnoreCase("ADMIN") ? "Admin" : "Cliente";
            permiso = permisoRepository.findByNombre(lookup)
                    .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + lookup));
        }

        Usuario cambios = new Usuario();
        cambios.setNombres(req.getNombres());
        cambios.setApellidos(req.getApellidos());
        cambios.setEmail(req.getEmail());
        cambios.setRut(req.getRut());
        cambios.setEstado(Boolean.TRUE.equals(req.getEstado()));
        if (permiso != null) cambios.setPermiso(permiso);
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            cambios.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        Usuario usuario = usuarioService.actualizar(id, cambios);
        // No exponer el hash del password en la respuesta
        usuario.setPassword(null);
        return ResponseEntity.ok(usuario);
    }

    // Actualizar solo el estado (habilitar/bloquear)
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> actualizarEstado(@PathVariable Long id, @RequestBody com.shopersa.backend_shopersa.MS_Usuario.dto.EstadoUpdateRequest req) {
        boolean nuevoEstado = Boolean.TRUE.equals(req.getEstado());
        Usuario u = usuarioService.actualizarEstado(id, nuevoEstado);
        u.setPassword(null);
        return ResponseEntity.ok(u);
    }

    // Crear usuario (Admin)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearUsuario(@RequestBody AdminCreateUserRequest req) {
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email es requerido");
        }
        if (req.getPassword() == null || req.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password es requerido");
        }
        String permisoNombre = req.getPermiso();
        if (permisoNombre == null || permisoNombre.isBlank()) {
            permisoNombre = "CLIENTE";
        }
        String lookup = permisoNombre.equalsIgnoreCase("ADMIN") ? "Admin" : "Cliente";
        Permiso permiso = permisoRepository.findByNombre(lookup)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado: " + lookup));

        Usuario nuevo = new Usuario();
        nuevo.setNombres(req.getNombres());
        nuevo.setApellidos(req.getApellidos());
        nuevo.setEmail(req.getEmail());
        nuevo.setRut(req.getRut());
        nuevo.setEstado(Boolean.TRUE.equals(req.getEstado()));
        nuevo.setPassword(passwordEncoder.encode(req.getPassword()));
        nuevo.setPermiso(permiso);

        Usuario creado = usuarioService.crear(nuevo);
        creado.setPassword(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Eliminar usuario (Admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}