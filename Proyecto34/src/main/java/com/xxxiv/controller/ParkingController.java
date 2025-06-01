package com.xxxiv.controller;

import com.xxxiv.dto.FiltroNoticiasDTO;
import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.dto.NoticiaDTO;
import com.xxxiv.dto.ParkingDTO;
import com.xxxiv.model.Noticia;
import com.xxxiv.model.Parking;
import com.xxxiv.service.ParkingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parkings")
@SecurityRequirement(name = "bearerAuth")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * GET /api/parkings
     * Devuelve todos los parkings, con sus polígonos.
     */
    @GetMapping
    public ResponseEntity<Page<ParkingDTO>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) Integer capacidadMaxima,
            Pageable pageable
    ) {
        int maxPageSize = 50;
        int size = pageable.getPageSize() > maxPageSize
                ? maxPageSize
                : pageable.getPageSize();
        Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

        FiltroParkingDTO filtro = new FiltroParkingDTO();
        filtro.setCapacidadMinima(capacidadMinima);
        filtro.setCapacidadMaxima(capacidadMaxima);
        filtro.setName(name);

        Page<Parking> pageEntidades = parkingService.findAll(filtro, safePageable);

        Page<ParkingDTO> pageDto = pageEntidades.map(ParkingDTO::fromEntity);

        return ResponseEntity.ok(pageDto);
    }

    /**
     * GET /api/parkings/{id}
     * Devuelve un parking por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Parking> getById(@PathVariable Integer id) {
        try {
            Parking parking = parkingService.findById(id);
            return ResponseEntity.ok(parking);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * POST /api/parkings
     * Crea un nuevo parking (sin puntos). El JSON de entrada:
     * {
     * "name": "Parking Norte",
     * "capacity": 30
     * }
     */
    @PostMapping
    public ResponseEntity<Parking> create(@RequestBody ParkingDTO payload) {
        Parking created = parkingService.create(payload);
        return ResponseEntity.status(201).body(created);
    }

    /**
     * PUT /api/parkings/{id}
     * Actualiza solo los campos name y capacity. El JSON de entrada:
     * {
     * "name": "Nuevo Nombre",
     * "capacity": 50
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Parking> update(
            @PathVariable Integer id,
            @RequestBody ParkingDTO payload
    ) {
        try {
            Parking updated = parkingService.update(id, payload);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/parkings/{id}
     * Elimina un parking por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            parkingService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

}
