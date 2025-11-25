package com.shopersa.backend_shopersa.MS_Usuario.dto;

import lombok.Data;

@Data
public class AdminCreateUserRequest {
    private String nombres;
    private String apellidos;
    private String email;
    private String rut;
    private Boolean estado; // true/false
    private String permiso; // "ADMIN" o "CLIENTE"
    private String password; // requerido
}