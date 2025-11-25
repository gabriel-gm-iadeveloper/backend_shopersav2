package com.shopersa.backend_shopersa.MS_TipoEnvio.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopersa.backend_shopersa.MS_Envio.entities.Envio;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class TipoEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre; // Ej: "Despacho a Domicilio", "Retiro en Tienda"

    private String descripcion;
    private double costo;

    @OneToMany(mappedBy = "tipoEnvio")
    @JsonIgnore
    private List<Envio> envios;
}