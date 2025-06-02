package com.xxxiv.service;

import com.xxxiv.model.Calificacion;
import com.xxxiv.repository.CalificacionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalificacionService {
    @Autowired
    CalificacionRepository calificacionRepository;

    
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
    @Transactional
    public List<Calificacion> buscarPorIdVehiculo(Integer id) {
        return calificacionRepository.findByVehiculoId(id);
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
