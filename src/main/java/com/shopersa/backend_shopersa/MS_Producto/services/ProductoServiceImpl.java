package com.shopersa.backend_shopersa.MS_Producto.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopersa.backend_shopersa.MS_Categoria.entities.Categoria;
import com.shopersa.backend_shopersa.MS_Categoria.repositories.CategoriaRepository;
import com.shopersa.backend_shopersa.MS_Producto.dto.ProductoDTO;
import com.shopersa.backend_shopersa.MS_Producto.entities.ProductImage;
import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;
import com.shopersa.backend_shopersa.MS_Producto.repositories.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired private ProductoRepository productoRepository;
    @Autowired private CategoriaRepository categoriaRepository;

    @Override
    public List<Producto> findAll() { return (List<Producto>) productoRepository.findAll(); }

    @Override
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Override
    public void deleteById(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    public Producto save(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getCategoriaId()));

        Producto p = new Producto();
        mapDtoToEntity(dto, p, categoria);
        validarStockNoNegativo(p.getStock());
        return productoRepository.save(p);
    }

    @Override
    public Producto update(Long id, ProductoDTO dto) {
        Producto p = findById(id);
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getCategoriaId()));
        mapDtoToEntity(dto, p, categoria);
        validarStockNoNegativo(p.getStock());
        return productoRepository.save(p);
    }

    @Override
    public Producto desactivar(Long id) {
        Producto p = findById(id);
        p.setEstadoProducto(false);
        return productoRepository.save(p);
    }

    @Override
    public Producto activar(Long id) {
        Producto p = findById(id);
        p.setEstadoProducto(true);
        return productoRepository.save(p);
    }

    @Override
    public Producto ajustarStock(Long id, int delta) {
        Producto p = findById(id);
        int nuevo = p.getStock() + delta;
        if (nuevo < 0) throw new RuntimeException("Stock insuficiente");
        p.setStock(nuevo);
        return productoRepository.save(p);
    }

    @Override
    public Producto addImages(Long id, List<String> urls) {
        Producto p = findById(id);
        int baseOrden = p.getImagenes() == null ? 0 : p.getImagenes().size();
        if (p.getImagenes() == null) p.setImagenes(new ArrayList<>());
        int count = baseOrden;
        for (String u : urls) {
            if (u == null || u.isBlank()) continue;
            if (count >= 3) break; 
            ProductImage img = new ProductImage();
            img.setProducto(p);
            img.setUrl(u);
            img.setOrden(count++);
            p.getImagenes().add(img);
        }
        return productoRepository.save(p);
    }

    @Override
    public Producto removeImage(Long id, String url) {
        Producto p = findById(id);
        if (p.getImagenes() != null) {
            p.getImagenes().removeIf(pi -> pi != null && url.equals(pi.getUrl()));
        }
        return productoRepository.save(p);
    }

    @Override
    public Producto addProductImages(Long id, List<String> urls) {
        return addImages(id, urls);
    }

    @Override
    public Producto removeProductImage(Long productoId, Long imageId) {
        Producto p = findById(productoId);
        if (p.getImagenes() != null) {
            p.getImagenes().removeIf(pi -> pi != null && imageId.equals(pi.getId()));
        }
        return productoRepository.save(p);
    }

    @Override
    public Producto reorderImages(Long productoId, List<Long> imageIdsOrder) {
        Producto p = findById(productoId);
        if (p.getImagenes() == null) return p;
        for (int i = 0; i < imageIdsOrder.size(); i++) {
            Long idImg = imageIdsOrder.get(i);
            for (ProductImage pi : p.getImagenes()) {
                if (pi.getId() != null && pi.getId().equals(idImg)) {
                    pi.setOrden(i);
                    break;
                }
            }
        }
        p.getImagenes().sort(Comparator.comparing(pi -> pi.getOrden() == null ? Integer.MAX_VALUE : pi.getOrden()));
        return productoRepository.save(p);
    }


    private void mapDtoToEntity(ProductoDTO dto, Producto entity, Categoria categoria) {
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPrecio(dto.getPrecio());
        entity.setStock(dto.getStock());
        entity.setPrecio_oferta(dto.getPrecio_oferta());
        entity.setEs_oferta(dto.isEs_oferta());
        entity.setEs_novedad(dto.isEs_novedad());
        entity.setEstadoProducto(dto.isEstadoProducto());
        entity.setCategoria(categoria);
        entity.setCodigo_producto(dto.getCodigo_producto());
        entity.setTipo_ropa(dto.getTipo_ropa());
        entity.setTalla(dto.getTalla());
        entity.setGenero(dto.getGenero());
        entity.setTipo_juguete(dto.getTipo_juguete());
        entity.setTamano(dto.getTamano());
        entity.setMaterial(dto.getMaterial());
        entity.setMarca(dto.getMarca());
    }

    private void validarStockNoNegativo(int stock) {
        if (stock < 0) throw new RuntimeException("El stock no puede ser negativo");
    }
}
