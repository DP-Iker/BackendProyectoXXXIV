package com.xxxiv.controller;

import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.dto.ParkingDTO;
import com.xxxiv.model.Parking;
import com.xxxiv.service.ParkingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parkings")
public class ParkingController {

    private final ParkingService parkingService;


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
            Parking parking = parkingService.obtenerParkingPorId(id);
            return ResponseEntity.ok(parking);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Parking> create(@RequestBody ParkingDTO payload) {
        Parking created = parkingService.create(payload);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
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
    @PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        try {
            parkingService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
