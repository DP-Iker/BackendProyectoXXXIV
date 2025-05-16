package com.xxxiv.service;

import com.xxxiv.model.Vehiculo;
import com.xxxiv.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> findAll() {
        List<Vehiculo> lista = vehiculoRepository.findAll();
        System.out.println("Vehículos encontrados: " + lista.size());
        return lista;
    }
    
    public Optional<Vehiculo> findById(int id) {
        return vehiculoRepository.findById(id);
    }
}
