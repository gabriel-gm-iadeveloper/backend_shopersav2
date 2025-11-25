package com.shopersa.backend_shopersa.MS_Envio.services;

import com.shopersa.backend_shopersa.MS_Envio.dto.EnvioDTO;
import com.shopersa.backend_shopersa.MS_Envio.entities.Envio;
import com.shopersa.backend_shopersa.MS_Envio.repositories.EnvioRepository;
import com.shopersa.backend_shopersa.MS_TipoEnvio.entities.TipoEnvio;
import com.shopersa.backend_shopersa.MS_TipoEnvio.repositories.TipoEnvioRepository;
import com.shopersa.backend_shopersa.MS_Usuario.entities.Usuario;
import com.shopersa.backend_shopersa.MS_Usuario.repositories.UsuarioRepository;
import com.shopersa.backend_shopersa.MS_Venta.entities.Venta;
import com.shopersa.backend_shopersa.MS_Venta.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvioService {
    @Autowired private EnvioRepository envioRepository;
    @Autowired private VentaRepository ventaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private TipoEnvioRepository tipoEnvioRepository;

    public Envio crearEnvio(EnvioDTO envioDTO, String usuarioEmail) {
        Venta venta = ventaRepository.findById(envioDTO.getVentaId())
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        Usuario usuario = usuarioRepository.findByEmail(usuarioEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TipoEnvio tipoEnvio = tipoEnvioRepository.findById(envioDTO.getTipoEnvioId())
                .orElseThrow(() -> new RuntimeException("Tipo de envío no encontrado"));

        Envio envio = new Envio();
        envio.setVenta(venta);
        envio.setUsuario(usuario);
        envio.setTipoEnvio(tipoEnvio);
        envio.setDireccion(envioDTO.getDireccion());
        envio.setEstado("En preparación");

        return envioRepository.save(envio);
    }
    
    public List<Envio> findByUsuarioEmail(String email) {
        return envioRepository.findByUsuarioEmail(email);
    }

    public List<Envio> findAll() {
        return (List<Envio>) envioRepository.findAll();
    }
    
    public Envio updateEstado(Long envioId, String nuevoEstado) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new RuntimeException("Envio no encontrado"));
        envio.setEstado(nuevoEstado);
        return envioRepository.save(envio);
    }
}