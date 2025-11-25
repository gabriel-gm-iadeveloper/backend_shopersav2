package com.shopersa.backend_shopersa.MS_Usuario.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shopersa.backend_shopersa.MS_Envio.entities.Envio;
import com.shopersa.backend_shopersa.MS_Permisos.entities.Permiso;
import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String rut;
    private String nombres;
    private String apellidos;
    private String email;
    private String password;
    private LocalDate fecha_nacimiento;
    private String telefono;
    private String direccion_calle;
    private String direccion_numero;
    private String comuna;
    private String region;
    private boolean estado;

    @CreationTimestamp
    private LocalDateTime fecha_creacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="permiso_id")
    private Permiso permiso;

     
    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference("usuario-ventas")
    private List<Venta> ventas;

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference("usuario-envios")
    private List<Envio> envios;
    
    
}
