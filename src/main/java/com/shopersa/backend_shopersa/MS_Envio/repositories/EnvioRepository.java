package com.shopersa.backend_shopersa.MS_Envio.repositories;
import com.shopersa.backend_shopersa.MS_Envio.entities.Envio;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface EnvioRepository extends CrudRepository<Envio, Long> {
    List<Envio> findByUsuarioEmail(String email);
}