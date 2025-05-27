package com.xxxiv.service;

import com.xxxiv.model.Carnet;
import com.xxxiv.repository.CarnetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarnetService {

    private final CarnetRepository carnetRepository;

    public CarnetService(CarnetRepository carnetRepository) {
        this.carnetRepository = carnetRepository;
    }

    public List<Carnet> findAll() {
        return carnetRepository.findAll();
    }

    public Optional<Carnet> findById(Integer usuarioId) {
        return carnetRepository.findById(usuarioId);
    }

    public Carnet save(Carnet carnet) {
        return carnetRepository.save(carnet);
    }

    public void deleteById(Integer usuarioId) {
        carnetRepository.deleteById(usuarioId);
    }
}
