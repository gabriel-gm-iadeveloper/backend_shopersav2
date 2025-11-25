package com.shopersa.backend_shopersa.MS_Usuario.dto;

public class EstadoUpdateRequest {
    private Boolean estado;
    public EstadoUpdateRequest() {}
    public EstadoUpdateRequest(Boolean estado) { this.estado = estado; }
    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}