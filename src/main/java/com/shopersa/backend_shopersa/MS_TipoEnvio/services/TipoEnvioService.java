package com.shopersa.backend_shopersa.MS_TipoEnvio.services;

import com.shopersa.backend_shopersa.MS_TipoEnvio.entities.TipoEnvio;
import com.shopersa.backend_shopersa.MS_TipoEnvio.repositories.TipoEnvioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoEnvioService {

    @Autowired
    private TipoEnvioRepository tipoEnvioRepository;

    public List<TipoEnvio> findAll() {
        return (List<TipoEnvio>) tipoEnvioRepository.findAll();
    }

    public TipoEnvio save(TipoEnvio tipoEnvio) {
        return tipoEnvioRepository.save(tipoEnvio);
    }
    
    public void delete(Long id) {
        tipoEnvioRepository.deleteById(id);
    }
}