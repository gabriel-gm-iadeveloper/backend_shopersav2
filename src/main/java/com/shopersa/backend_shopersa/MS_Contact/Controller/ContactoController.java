package com.shopersa.backend_shopersa.MS_Contact.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shopersa.backend_shopersa.MS_Contact.Dto.ContactoDto;
import com.shopersa.backend_shopersa.MS_Contact.Service.EmailService;

@RestController
@RequestMapping("/api/contacto")
public class ContactoController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> recibirMensajeContacto(@RequestBody ContactoDto contactoDto) {
        try {
            emailService.enviarCorreoContacto(
                contactoDto.getNombre(),
                contactoDto.getEmail(),
                contactoDto.getMensaje()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Loguear el error para depuración
            System.err.println("Error al enviar correo: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
