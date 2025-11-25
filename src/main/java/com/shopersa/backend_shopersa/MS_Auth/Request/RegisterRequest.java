package com.shopersa.backend_shopersa.MS_Auth.Request;

import java.time.LocalDate;
import lombok.Data;

@Data
public class RegisterRequest {
    //Campos necesarios para el registro
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private LocalDate fecha_nacimiento;
    private String telefono;
    private String rut;
    private String direccion_calle;
    private String direccion_numero;
    private String comuna;
    private String region;
    //No incluimos 'estado' ni 'permiso' ya que son asignados por el backend
}