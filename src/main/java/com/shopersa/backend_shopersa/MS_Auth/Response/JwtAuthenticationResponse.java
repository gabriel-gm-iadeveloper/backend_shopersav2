package com.shopersa.backend_shopersa.MS_Auth.Response;

import java.time.LocalDate;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
public class JwtAuthenticationResponse {
    private String token;
    private String tokenType = "Bearer";
    private UserInfo user;

    public JwtAuthenticationResponse(String accessToken, UserInfo userInfo) {
        this.token = accessToken;
        this.user = userInfo;
    }

    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String email;
        private String nombres;
        private String apellidos;
        private String role;
        private boolean isAdmin;

        private String rut;
        private LocalDate fecha_nacimiento;
        private String telefono;
        private String direccion_calle;
        private String direccion_numero;
        private String comuna;
        private String region;
    }
}