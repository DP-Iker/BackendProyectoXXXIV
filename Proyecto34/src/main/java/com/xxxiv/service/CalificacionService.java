package com.xxxiv.service;

import com.xxxiv.dto.CalificacionDTO;
import com.xxxiv.model.Calificacion;
import com.xxxiv.model.Usuario;
import com.xxxiv.repository.CalificacionRepository;
import com.xxxiv.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalificacionService {
    @Autowired
    CalificacionRepository calificacionRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    
    @Transactional
    public Calificacion crear(Calificacion calificacion) {
        return calificacionRepository.save(calificacion);
    }

    
    @Transactional
    public Calificacion actualizar(Integer id, Calificacion calificacionActualizada) {
        Calificacion existente = calificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Calificación no encontrada con id=" + id));

        existente.setVehiculoId(calificacionActualizada.getVehiculoId());
        existente.setUsuarioId(calificacionActualizada.getUsuarioId());
        existente.setContenido(calificacionActualizada.getContenido());
        existente.setCalificacion(calificacionActualizada.getCalificacion());
        // Nota: createdAt no se modifica aquí

        return calificacionRepository.save(existente);
    }

    
    @Transactional
    public void eliminar(Integer id) {
        Calificacion existente = calificacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Calificación no encontrada con id=" + id));
        calificacionRepository.delete(existente);
    }
//    @Transactional
//    public List<Calificacion> buscarPorIdVehiculo(Integer id) {
//        return calificacionRepository.findByVehiculoId(id);
//    }

    @Transactional
    public List<CalificacionDTO> buscarPorIdVehiculo(Integer idVehiculo) {
        List<Calificacion> todas = calificacionRepository.findByVehiculoId(idVehiculo);

        return todas.stream().map(calificacion -> {
            CalificacionDTO dto = new CalificacionDTO();
            // Datos básicos de la Calificacion:
            dto.setId(calificacion.getId());
            dto.setVehiculoId(calificacion.getVehiculoId());
            dto.setContenido(calificacion.getContenido());
            dto.setCalificacion(calificacion.getCalificacion());
            dto.setCreatedAt(calificacion.getCreatedAt());

            // Obtenemos Usuario por ID:
            Integer usuarioId = calificacion.getUsuarioId();
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado ID=" + usuarioId));

            dto.setUsername(usuario.getUsuario());
            dto.setAvatar(usuario.getFoto());
            return dto;
        }).collect(Collectors.toList());
    }
    
    @Transactional
    public Optional<Calificacion> buscarPorId(Integer id) {
        return calificacionRepository.findById(id);
    }

    
    @Transactional
    public List<Calificacion> buscarTodas() {
        return calificacionRepository.findAll();
    }
}
