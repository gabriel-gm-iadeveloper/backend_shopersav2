package com.shopersa.backend_shopersa.MS_Venta.services;

import com.shopersa.backend_shopersa.MS_Producto.entities.Producto;
import com.shopersa.backend_shopersa.MS_Producto.repositories.ProductoRepository;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;
import com.shopersa.backend_shopersa.MS_Venta.dto.DetalleVentaDTO;
import com.shopersa.backend_shopersa.MS_Venta.dto.VentaAdminResponseDTO;
import com.shopersa.backend_shopersa.MS_Venta.dto.VentaDTO;
import com.shopersa.backend_shopersa.MS_Venta.entities.DetalleVenta;
import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;
import com.shopersa.backend_shopersa.MS_Venta.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public Venta crearVenta(VentaDTO ventaDTO, String usuarioEmail) {
        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();
        venta.setUsuario(usuario);
        venta.setEstado("COMPLETADA");
        
        venta.setTotal(ventaDTO.getTotal());

        for (DetalleVentaDTO detalleDTO : ventaDTO.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalleDTO.getProductoId()));

            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            DetalleVenta detalleVenta = new DetalleVenta();
            detalleVenta.setProducto(producto);
            detalleVenta.setCantidadVendido(detalleDTO.getCantidad());
            detalleVenta.setPrecioUnitarioProducto(producto.isEs_oferta() ? producto.getPrecio_oferta() : producto.getPrecio());
            detalleVenta.setVenta(venta);
            
            venta.getDetallesVenta().add(detalleVenta);
        }

        return ventaRepository.save(venta);
    }

    @Override
    public List<Venta> findVentasByUsuarioEmail(String usuarioEmail) {
        return ventaRepository.findByUsuarioEmail(usuarioEmail);
    }

    @Override
    public List<VentaAdminResponseDTO> findAllVentas() {
        List<Venta> ventas = (List<Venta>) ventaRepository.findAll();
        
        // Mapeamos la lista de Entidades a una lista de DTOs
        return ventas.stream().map(venta -> {
            VentaAdminResponseDTO dto = new VentaAdminResponseDTO();
            dto.setId(venta.getId());
            dto.setFecha_creacion(venta.getFecha_creacion());
            dto.setTotal(venta.getTotal());
            dto.setEstado(venta.getEstado());

            if (venta.getUsuario() != null) {
                VentaAdminResponseDTO.UsuarioSimpleDTO userDto = new VentaAdminResponseDTO.UsuarioSimpleDTO();
                userDto.setId(venta.getUsuario().getId());
                userDto.setEmail(venta.getUsuario().getEmail());
                dto.setUsuario(userDto);
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
}