package com.shopersa.backend_shopersa.MS_Envio.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shopersa.backend_shopersa.MS_TipoEnvio.entities.TipoEnvio;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime hora_creada;

    private String direccion; 
    private String estado; 

    @OneToOne
    @JoinColumn(name = "venta_id", referencedColumnName = "id", unique = true)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario-envios")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tipo_envio_id")
    private TipoEnvio tipoEnvio;
}