package com.shopersa.backend_shopersa.MS_Producto.dto;

import java.util.List;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductoDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser un número positivo")
    private Double precio;

    @NotNull(message = "El stock no puede ser nulo")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    private double precio_oferta;
    private boolean es_oferta;
    private boolean es_novedad;
    private boolean estadoProducto;

    @NotNull(message = "El ID de la categoría no puede ser nulo")
    private Long categoriaId;

    private String codigo_producto;
    private String tipo_ropa;
    private String talla;
    private String genero;
    private String tipo_juguete;
    private String tamano;
    private String material;
    private String marca;
    
    @Size(max = 3, message = "No se pueden agregar más de 3 imágenes")
    private List<String> imagenes;
}