package com.shopersa.backend_shopersa.MS_Venta.services;

import com.shopersa.backend_shopersa.MS_Venta.dto.VentaAdminResponseDTO;
import com.shopersa.backend_shopersa.MS_Venta.dto.VentaDTO;
import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;
import java.util.List;

public interface VentaService {
    Venta crearVenta(VentaDTO ventaDTO, String usuarioEmail);
    List<Venta> findVentasByUsuarioEmail(String usuarioEmail);
    List<VentaAdminResponseDTO> findAllVentas();
}