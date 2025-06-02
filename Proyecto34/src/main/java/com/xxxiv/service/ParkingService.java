
package com.xxxiv.service;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.model.Parking;
import com.xxxiv.repository.ParkingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;

    public List<Parking> buscarTodosLosParkings() {
        return parkingRepository.findAll();
    }

    public Optional<Parking> buscarPorId(int id) {
        return parkingRepository.findById(id);
    }

    public Parking crearParking(Parking parking) {
        return parkingRepository.save(parking);
    }

    public Parking actualizarParking(int id, Parking datos) {
        Parking existente = buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking no encontrado"));

        existente.setName(datos.getName());
        existente.setCapacity(datos.getCapacity());

        return parkingRepository.save(existente);
    }
}
