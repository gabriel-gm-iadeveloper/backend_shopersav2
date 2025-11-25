package com.shopersa.backend_shopersa.MS_Venta.dto;

import lombok.Data;
import java.util.List;

@Data
public class VentaDTO {
    // El frontend solo necesita enviar la lista de productos y sus cantidades
    private List<DetalleVentaDTO> detalles;
    private double total;
}