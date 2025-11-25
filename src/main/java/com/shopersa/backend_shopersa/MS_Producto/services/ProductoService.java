package com.shopersa.backend_shopersa.MS_Producto.services;

import java.util.List;
import com.shopersa.backend_shopersa.MS_Producto.dto.ProductoDTO;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;

public interface ProductoService {
    List<Producto> findAll();
    Producto findById(Long id);
    Producto save(ProductoDTO productoDTO);
    Producto update(Long id, ProductoDTO productoDTO);
    void deleteById(Long id);
    Producto desactivar(Long id);
    Producto activar(Long id);

    Producto ajustarStock(Long id, int delta);
    Producto addImages(Long id, List<String> urls);
    Producto removeImage(Long id, String url);
    Producto addProductImages(Long id, List<String> urls);
    Producto removeProductImage(Long productoId, Long imageId);
    Producto reorderImages(Long productoId, List<Long> imageIdsOrder);
}