package com.shopersa.backend_shopersa.MS_Auth.Request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}