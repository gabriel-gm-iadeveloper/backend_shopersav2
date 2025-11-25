// src/main/java/com/shopersa/backend_shopersa/MS_Venta/entities/Venta.java
package com.shopersa.backend_shopersa.MS_Venta.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference; // <-- IMPORTAR
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime fecha_creacion;

    private double total;
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-ventas")
    private Usuario usuario;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // <-- AÑADIR ESTA ANOTACIÓN
    private List<DetalleVenta> detallesVenta = new ArrayList<>();
}