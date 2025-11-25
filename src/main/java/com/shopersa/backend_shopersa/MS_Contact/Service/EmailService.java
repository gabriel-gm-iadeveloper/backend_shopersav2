package com.shopersa.backend_shopersa.MS_Contact.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void enviarCorreoContacto(String nombre, String emailRemitente, String mensaje) throws IOException {
        // El correo desde el que se envía. DEBE SER UN CORREO VERIFICADO EN SENDGRID.
        // Para empezar, puedes usar el mismo con el que te registraste.
        Email from = new Email("contacto.bleysters@gmail.com"); 
        String subject = "Nuevo Mensaje de Contacto de: " + nombre;
        
        // El correo al que llegará el mensaje
        Email to = new Email("gabr.gonzalezm@duocuc.cl");

        // Construimos el cuerpo del mensaje
        String cuerpoMensaje = "Has recibido un nuevo mensaje desde el formulario de contacto de ShoPersa.\n\n"
                             + "Nombre: " + nombre + "\n"
                             + "Correo del remitente: " + emailRemitente + "\n\n"
                             + "Mensaje:\n"
                             + "----------------------------------\n"
                             + mensaje
                             + "\n----------------------------------";
        
        Content content = new Content("text/plain", cuerpoMensaje);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println("Status Code de SendGrid: " + response.getStatusCode()); // Para depurar
        } catch (IOException ex) {
            System.err.println("Error al enviar correo con SendGrid: " + ex.getMessage());
            throw ex;
        }
    }
}
