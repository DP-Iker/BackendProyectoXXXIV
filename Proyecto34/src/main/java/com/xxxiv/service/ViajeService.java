package com.xxxiv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ViajeRepository;

@Service
public class ViajeService {

    private final ViajeRepository viajeRepository;

    public ViajeService(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }

    public List<Viaje> findAll() {
        return viajeRepository.findAll();
    }

    public Optional<Viaje> buscarPorId(Integer id) {
        return viajeRepository.findById(id);
    }

    public Viaje save(Viaje viaje) {
        return viajeRepository.save(viaje);
    }

    public void deleteById(Integer id) {
        viajeRepository.deleteById(id);
    }
}
