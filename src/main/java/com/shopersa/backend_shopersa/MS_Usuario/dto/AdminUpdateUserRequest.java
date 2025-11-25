package com.shopersa.backend_shopersa.MS_Usuario.dto;

public class AdminUpdateUserRequest {
    private String nombres;
    private String apellidos;
    private String email;
    private String rut;
    private Boolean estado;
    // Permiso como string: "ADMIN" o "CLIENTE"
    private String permiso;
    // Password opcional (si viene, se actualiza)
    private String password;

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
    public String getPermiso() { return permiso; }
    public void setPermiso(String permiso) { this.permiso = permiso; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}