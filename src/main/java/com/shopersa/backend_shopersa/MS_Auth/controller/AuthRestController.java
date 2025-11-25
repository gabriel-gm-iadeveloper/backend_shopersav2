package com.shopersa.backend_shopersa.MS_Auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.shopersa.backend_shopersa.MS_Auth.Request.LoginRequest;
import com.shopersa.backend_shopersa.MS_Auth.Request.RegisterRequest;
import com.shopersa.backend_shopersa.MS_Auth.Response.JwtAuthenticationResponse;
import com.shopersa.backend_shopersa.MS_Auth.Response.JwtAuthenticationResponse.UserInfo;
import com.shopersa.backend_shopersa.MS_Auth.Security.JwtProvider;
import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Permisos.repositories.PermisoRepository;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;
import com.shopersa.backend_shopersa.MS_Usuario.services.UsuarioService;

@CrossOrigin(originPatterns = "http://localhost:*")
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtProvider jwtProvider;
  @Autowired private UsuarioService usuarioService;
  @Autowired private UsuarioRepository usuarioRepository;
  @Autowired private PermisoRepository permisoRepositories;
  @Autowired private PasswordEncoder passwordEncoder;

  //=== LOGIN: devuelve JWT + datos del usuario ===
  @PostMapping("/login")
  public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtProvider.generateToken(authentication);

    Usuario u = usuarioRepository.findByEmail(loginRequest.getEmail())
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String role = (u.getPermiso() != null && u.getPermiso().getNombre() != null)
        ? u.getPermiso().getNombre().toUpperCase()
        : "CLIENTE";
    boolean isAdmin = "ADMIN".equals(role);


    //Mapeamos todos los campos del usuario a UserInfo
    UserInfo info = new UserInfo(
        u.getId(),
        u.getEmail(),
        u.getNombres(),
        u.getApellidos(),
        role,
        isAdmin,
        u.getRut(),
        u.getFecha_nacimiento(),
        u.getTelefono(),
        u.getDireccion_calle(),
        u.getDireccion_numero(),
        u.getComuna(),
        u.getRegion()
    );

    return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, info));
  }

  //=== PERFIL DEL USUARIO AUTENTICADO ===
   @GetMapping("/me")
  public ResponseEntity<UserInfo> me() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    email = email == null ? null : email.trim().toLowerCase();

    Usuario u = usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String role = (u.getPermiso() != null && u.getPermiso().getNombre() != null)
        ? u.getPermiso().getNombre().toUpperCase()
        : "CLIENTE";
    boolean isAdmin = "ADMIN".equals(role);
    
    //Creamos el UserInfo pasando TODOS los campos que espera el constructor.
    return ResponseEntity.ok(new UserInfo(
        u.getId(), 
        u.getEmail(), 
        u.getNombres(), 
        u.getApellidos(), 
        role, 
        isAdmin,
        u.getRut(),
        u.getFecha_nacimiento(),
        u.getTelefono(),
        u.getDireccion_calle(),
        u.getDireccion_numero(),
        u.getComuna(),
        u.getRegion()
    ));
  }

  //=== REGISTRO: crea usuario con password encriptada y rol 'Cliente' ===
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
    if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
      return new ResponseEntity<>("El email ya está en uso.", HttpStatus.BAD_REQUEST);
    }

    Usuario usuario = new Usuario();
    usuario.setNombres(registerRequest.getNombres());
    usuario.setApellidos(registerRequest.getApellidos());
    usuario.setEmail(registerRequest.getEmail());
    usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    usuario.setFecha_nacimiento(registerRequest.getFecha_nacimiento());
    usuario.setTelefono(registerRequest.getTelefono());
    usuario.setRut(registerRequest.getRut());
    usuario.setDireccion_calle(registerRequest.getDireccion_calle());
    usuario.setDireccion_numero(registerRequest.getDireccion_numero());
    usuario.setComuna(registerRequest.getComuna());
    usuario.setRegion(registerRequest.getRegion());
    usuario.setEstado(true);

    Permiso userPermiso = permisoRepositories.findByNombre("Cliente")
        .orElseThrow(() -> new RuntimeException("Error: Permiso 'Cliente' no encontrado."));
    usuario.setPermiso(userPermiso);

    usuarioService.crear(usuario);

    return new ResponseEntity<>("Usuario registrado exitosamente", HttpStatus.CREATED);
  }
}
