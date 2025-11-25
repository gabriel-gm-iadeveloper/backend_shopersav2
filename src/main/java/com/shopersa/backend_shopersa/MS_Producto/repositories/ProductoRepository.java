package com.shopersa.backend_shopersa.MS_Producto.repositories;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Long> {

    List<Producto> findByEstadoProductoIsTrueAndStockLessThanEqual(int stockLevel);
}