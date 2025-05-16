package com.xxxiv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.xxxiv.model.Caracteristica;
import com.xxxiv.repository.CaracteristicaRepository;

@Service
public class CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;

    public CaracteristicaService(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    public List<Caracteristica> obtenerTodas() {
        return caracteristicaRepository.findAll();
    }

    public Optional<Caracteristica> obtenerPorId(Integer vehiculoId) {
        return caracteristicaRepository.findById(vehiculoId);
    }

    public Caracteristica guardar(Caracteristica caracteristica) {
        return caracteristicaRepository.save(caracteristica);
    }

    public void eliminar(Integer vehiculoId) {
        caracteristicaRepository.deleteById(vehiculoId);
    }
}
