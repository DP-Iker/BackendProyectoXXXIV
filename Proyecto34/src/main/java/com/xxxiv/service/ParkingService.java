package com.xxxiv.service;

import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.dto.ParkingDTO;
import com.xxxiv.model.Noticia;
import com.xxxiv.model.Parking;
import com.xxxiv.model.ParkingPolygon;
import com.xxxiv.model.ParkingPolygonId;
import com.xxxiv.repository.ParkingPolygonRepository;
import com.xxxiv.repository.ParkingRepository;
import com.xxxiv.specifications.NoticiaSpecification;
import com.xxxiv.specifications.ParkingSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ParkingService {
    @Autowired
    ParkingRepository parkingRepository;
    @Autowired
    ParkingPolygonRepository parkingPolygonRepository;

    public ParkingService(ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Transactional(readOnly = true)
    public Page<Parking> findAll(FiltroParkingDTO filtro, Pageable pageable) {
        Specification<Parking> filtrosAplicados = ParkingSpecification.buildSpecification(filtro);

        return parkingRepository.findAll(filtrosAplicados, pageable);
    }


    @Transactional(readOnly = true)
    public Parking findById(Integer id) {
        return parkingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Parking no encontrado con id=" + id));
    }

    @Transactional
    public Parking create(ParkingDTO dto) {
        Parking parking = ParkingDTO.toEntity(dto);
        parking.setPolygonPoints(Collections.emptyList()); // vaciar por ahora

        Parking savedParking = parkingRepository.save(parking);

        List<ParkingPolygon> polygons = new ArrayList<>();
        int index = 0;
        for (double[] point : dto.getPolygon()) {
            ParkingPolygon polygon = new ParkingPolygon(savedParking.getId(), index++, point[0], point[1]);
            polygons.add(polygon);
        }

        savedParking.setPolygonPoints(polygons);

        return parkingRepository.save(savedParking);
    }

    @Transactional
    public Parking update(Integer id, ParkingDTO dto) {
        Parking parking = parkingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Parking not found"));

        parking.setName(dto.getName());
        parking.setCapacity(dto.getCapacity());


        parking.getPolygonPoints().clear(); // limpia la colección actual
        int index = 0;
        List<ParkingPolygon> newPolygons = new ArrayList<>();
        Integer parkingId = parking.getId();

        for (double[] point : dto.getPolygon()) {
            ParkingPolygon polygon = new ParkingPolygon(parkingId, index++, point[0], point[1]);
            newPolygons.add(polygon);
        }
        parking.getPolygonPoints().addAll(newPolygons);

        return parkingRepository.save(parking);
    }

    @Transactional
    public void delete(Integer id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado con id=" + id));


        parkingPolygonRepository.deleteAllByParkingId(id);

        parkingRepository.deleteById(id);
    }

    @Transactional
    public Parking replacePolygon(Integer parkingId, List<double[]> newPoints) {
        Parking parking = findById(parkingId);

        parking.clearPolygonPoints();

        for (int i = 0; i < newPoints.size(); i++) {
            double[] coords = newPoints.get(i);
            parking.addPolygonPoint(i, coords[0], coords[1]);
        }

        return parkingRepository.save(parking);
    }
}
