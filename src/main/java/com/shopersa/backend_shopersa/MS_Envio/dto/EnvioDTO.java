package com.shopersa.backend_shopersa.MS_Envio.dto;

import lombok.Data;

@Data
public class EnvioDTO {
    private Long ventaId;
    private Long tipoEnvioId;
    private String direccion; 
}