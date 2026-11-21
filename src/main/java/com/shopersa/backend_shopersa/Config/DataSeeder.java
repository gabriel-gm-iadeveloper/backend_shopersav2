
package com.shopersa.backend_shopersa.Config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Permisos.repositories.PermisoRepository;
import com.shopersa.backend_shopersa.MS_TipoEnvio.entities.TipoEnvio;
import com.shopersa.backend_shopersa.MS_TipoEnvio.repositories.TipoEnvioRepository;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TipoEnvioRepository tipoEnvioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.seed.enabled:false}")
    private boolean seedEnabled;

    @Value("${app.seed.admin.email:}")
    private String adminEmail;

    @Value("${app.seed.admin.password:}")
    private String adminPassword;

    @Value("${app.seed.admin.nombres:Administrador}")
    private String adminNombres;

    @Value("${app.seed.admin.apellidos:Sistema}")
    private String adminApellidos;

    @Override
    public void run(String... args) throws Exception {
        if (!seedEnabled) {
            System.out.println("Seed deshabilitado por configuración");
            return;
        }

        System.out.println("Iniciando seed de datos...");

        createPermisosIfNotExist();
        createTiposEnvioIfNotExist();
        createAdminUserIfNotExist();

        System.out.println("Seed completado exitosamente");
    }

    private void createTiposEnvioIfNotExist() {
        if (tipoEnvioRepository.findByNombre("Despacho a Domicilio").isEmpty()) {
            TipoEnvio despacho = new TipoEnvio();
            despacho.setNombre("Despacho a Domicilio");
            despacho.setDescripcion("Envío a través de empresa de transporte");
            despacho.setCosto(5000.0);
            tipoEnvioRepository.save(despacho);
        }

        if (tipoEnvioRepository.findByNombre("Retiro en Tienda").isEmpty()) {
            TipoEnvio retiro = new TipoEnvio();
            retiro.setNombre("Retiro en Tienda");
            retiro.setDescripcion("Retiro presencial en Persa Teniente Cruz");
            retiro.setCosto(0.0);
            tipoEnvioRepository.save(retiro);
        }
    }

    private void createPermisosIfNotExist() {
        if (permisoRepository.findByNombre("Admin").isEmpty()) {
            Permiso adminPermiso = new Permiso();
            adminPermiso.setNombre("Admin");
            adminPermiso.setDescripcion(
                "Administrador del sistema con acceso completo"
            );
            permisoRepository.save(adminPermiso);
        }

        if (permisoRepository.findByNombre("Cliente").isEmpty()) {
            Permiso clientePermiso = new Permiso();
            clientePermiso.setNombre("Cliente");
            clientePermiso.setDescripcion(
                "Cliente del sistema con acceso limitado"
            );
            permisoRepository.save(clientePermiso);
        }
    }

    private void createAdminUserIfNotExist() {

        if (adminEmail == null || adminEmail.isBlank()
                || adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                "Se requieren credenciales configuradas para crear el administrador"
            );
        }

        String email = adminEmail.trim();

        if (usuarioRepository.findByEmail(email).isEmpty()) {
            Permiso adminPermiso = permisoRepository.findByNombre("Admin")
                .orElseThrow(() ->
                    new IllegalStateException("Permiso Admin no encontrado")
                );

            Usuario admin = new Usuario();
            admin.setRut("11111111-1");
            admin.setNombres(adminNombres);
            admin.setApellidos(adminApellidos);
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setFecha_nacimiento(LocalDate.of(1990, 1, 1));
            admin.setTelefono("+56912345678");
            admin.setDireccion_calle("Calle Admin");
            admin.setDireccion_numero("123");
            admin.setComuna("Santiago");
            admin.setRegion("Metropolitana");
            admin.setEstado(true);
            admin.setPermiso(adminPermiso);

            usuarioRepository.save(admin);
            System.out.println("Usuario administrador creado: " + email);
        } else {
            System.out.println("El usuario administrador ya existe: " + email);
        }
    }
}
