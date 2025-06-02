package com.xxxiv.service;

import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.dto.ParkingDTO;
import com.xxxiv.model.Coordinate;
import com.xxxiv.model.Parking;
import com.xxxiv.repository.ParkingRepository;
import com.xxxiv.specifications.ParkingSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingService {
    @Autowired
    ParkingRepository parkingRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Transactional(readOnly = true)
    public Parking findById(Integer id) {
        return parkingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado con id=" + id));
    }

    @Transactional(readOnly = true)
    public Page<Parking> findAll(FiltroParkingDTO filtro, Pageable pageable) {
        var specification = ParkingSpecification.buildSpecification(filtro);
        return parkingRepository.findAll(specification, pageable);
    }

    @Transactional
    public Parking create(ParkingDTO dto) {
        Parking parking = ParkingDTO.toEntity(dto);

        List<Coordinate> coordinates = new ArrayList<>();
        for (double[] point : dto.getPolygon()) {
            coordinates.add(new Coordinate(point[0], point[1]));
        }

        parking.setCods(coordinates);

        return parkingRepository.save(parking);
    }

    @Transactional
    public Parking update(Integer id, ParkingDTO dto) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parking not found"));

        parking.setName(dto.getName());
        List<Coordinate> newCoordinates = new ArrayList<>();
        for (double[] point : dto.getPolygon()) {
            Coordinate coord = new Coordinate(point[0], point[1]);
            newCoordinates.add(coord);
        }

        parking.setCods(newCoordinates);

        return parkingRepository.save(parking);
    }

    @Transactional
    public void delete(Integer id) {
        // Verificamos que exista; si no, lanzamos excepción
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado con id=" + id));

        // Al eliminar el Parking, se borra también su campo JSON con las coordenadas
        parkingRepository.delete(parking);
    }

    @Transactional
    public Parking replacePolygon(Integer parkingId, List<double[]> newPoints) {
        // Obtenemos el Parking o lanzamos excepción si no existe
        Parking parking = findById(parkingId);

        // Creamos la nueva lista de Coordinate a partir de los puntos recibidos
        List<Coordinate> newCoordinates = new ArrayList<>();
        for (double[] coords : newPoints) {
            // Si necesitas incluir un time, reemplaza el tercer parámetro por el valor correspondiente.
            newCoordinates.add(new Coordinate(coords[0], coords[1]));
        }

        // Reemplazamos directamente el campo JSON con la nueva lista
        parking.setCods(newCoordinates);

        return parkingRepository.save(parking);
    }

}
