// src/main/java/com/shopersa/backend_shopersa/MS_Venta/dto/VentaAdminResponseDTO.java
package com.shopersa.backend_shopersa.MS_Venta.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VentaAdminResponseDTO {

    private Long id;
    private LocalDateTime fecha_creacion;
    private double total;
    private String estado;
    private UsuarioSimpleDTO usuario;

    // DTO interno para no exponer todos los datos del usuario
    @Data
    public static class UsuarioSimpleDTO {
        private Long id;
        private String email;
    }
}