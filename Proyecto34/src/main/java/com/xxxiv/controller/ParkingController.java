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


    @GetMapping
    public ResponseEntity<Page<ParkingDTO>> getAll(
            @RequestParam(required = false) String name,
            Pageable pageable
    ) {
        // Limitar tamaño máximo de página
        int maxPageSize = 50;
        int size = Math.min(pageable.getPageSize(), maxPageSize);
        Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

        // Construir el DTO de filtro (solo tiene 'name' ya que 'capacidad' fue eliminado)
        FiltroParkingDTO filtro = new FiltroParkingDTO();
        filtro.setName(name);

        // Obtener la página de entidades
        Page<Parking> pageEntidades = parkingService.findAll(filtro, safePageable);

        // Mapear a DTO
        Page<ParkingDTO> pageDto = pageEntidades.map(ParkingDTO::fromEntity);

        return ResponseEntity.ok(pageDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Parking> getById(@PathVariable Integer id) {
        try {
            Parking parking = parkingService.findById(id);
            return ResponseEntity.ok(parking);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    public ResponseEntity<Parking> create(@RequestBody ParkingDTO payload) {
        Parking created = parkingService.create(payload);
        return ResponseEntity.status(201).body(created);
    }

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
