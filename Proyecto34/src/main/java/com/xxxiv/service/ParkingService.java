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

    /**
     * Devuelve todos los parkings almacenados (incluyendo sus polígonos).
     */
    @Transactional(readOnly = true)
    public Page<Parking> findAll(FiltroParkingDTO filtro, Pageable pageable) {
        Specification<Parking> filtrosAplicados = ParkingSpecification.buildSpecification(filtro);

        return parkingRepository.findAll(filtrosAplicados, pageable);
    }

    /**
     * Recupera un parking por su ID. Lanza EntityNotFoundException si no existe.
     */
    @Transactional(readOnly = true)
    public Parking findById(Integer id) {
        return parkingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Parking no encontrado con id=" + id));
    }

    /**
     * Crea un nuevo parking (sin puntos de polígono inicialmente).
     */
//    @Transactional
//    public Parking create(ParkingDTO dto) {
//        // Convertir DTO a entidad Parking
//        Parking parkingEntity = ParkingDTO.toEntity(dto); // suponiendo que tienes este método
//        // Guardar en la base de datos
//        return parkingRepository.save(parkingEntity);
//    }

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
    /**
     * Actualiza el parking existente (solo campos name y capacity).
     * Para actualizar el polígono, usar métodos separados.
     */
    @Transactional
    public Parking update(Integer id, ParkingDTO dto) {

        Parking parking = parkingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Parking not found"));

        parking.setName(dto.getName());
        parking.setCapacity(dto.getCapacity());


//        parkingPolygonRepository.deleteByParkingId(parking.getId());
        parking.getPolygonPoints().clear(); // limpia la colección actual
        int index = 0;
        List<ParkingPolygon> newPolygons = new ArrayList<>();
        Integer parkingId = parking.getId();

        for (double[] point : dto.getPolygon()) {
            ParkingPolygon polygon = new ParkingPolygon(parkingId, index++, point[0], point[1]);
            newPolygons.add(polygon);
        }
        parking.getPolygonPoints().addAll(newPolygons);
//        parking.setPolygonPoints(newPolygons);

        return parkingRepository.save(parking);
    }

    /**
     * Elimina un parking y todos sus puntos en cascada.
     */
    @Transactional
    public void delete(Integer id) {
        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Parking no encontrado con id=" + id));


        parkingPolygonRepository.deleteAllByParkingId(id);

        parkingRepository.deleteById(id);
    }

    /**
     * Reemplaza el polígono completo de un parking dado.
     * Borra los puntos antiguos y guarda los nuevos en el orden proporcionado.
     */
    @Transactional
    public Parking replacePolygon(Integer parkingId, List<double[]> newPoints) {
        Parking parking = findById(parkingId);

        // Limpiar puntos antiguos
        parking.clearPolygonPoints();

        // Añadir nuevos puntos
        for (int i = 0; i < newPoints.size(); i++) {
            double[] coords = newPoints.get(i);
            parking.addPolygonPoint(i, coords[0], coords[1]);
        }

        return parkingRepository.save(parking);
    }
}
