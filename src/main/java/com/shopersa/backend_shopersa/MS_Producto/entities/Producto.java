package com.shopersa.backend_shopersa.MS_Producto.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;
import com.shopersa.backend_shopersa.MS_Venta.entities.DetalleVenta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo_producto;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;
    private double precio_oferta;
    private boolean es_oferta;
    private boolean es_novedad;
    private boolean estadoProducto; 

    private String tipo_ropa;
    private String talla;
    private String genero;
    private String tipo_juguete;
    private String tamano;
    private String material;
    private String marca;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fecha_creacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> imagenes;

    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<DetalleVenta> detallesVenta;
    

}