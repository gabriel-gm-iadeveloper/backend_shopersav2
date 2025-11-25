package com.shopersa.backend_shopersa.MS_Venta.repositories;

import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface VentaRepository extends CrudRepository<Venta, Long> {
    // Método para buscar el historial de compras de un usuario
    List<Venta> findByUsuarioEmail(String email);
}