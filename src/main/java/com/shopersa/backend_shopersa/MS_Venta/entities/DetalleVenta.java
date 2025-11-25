// src/main/java/com/shopersa/backend_shopersa/MS_Venta/entities/DetalleVenta.java
package com.shopersa.backend_shopersa.MS_Venta.entities;

import com.fasterxml.jackson.annotation.JsonBackReference; // <-- IMPORTAR
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidadVendido;
    private double precioUnitarioProducto;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    @JsonBackReference // <-- REEMPLAZAR @JsonIgnore CON ESTA ANOTACIÓN
    private Venta venta;
}