package com.xxxiv.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.CrearParkingDTO;
import com.xxxiv.dto.ParkingDTO;
import com.xxxiv.model.Parking;
import com.xxxiv.service.ParkingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/parkings")
@RequiredArgsConstructor
public class ParkingController {

	private final ParkingService parkingService;

	@GetMapping
	public ResponseEntity<List<ParkingDTO>> getAllParkings() {
		List<Parking> parkings = parkingService.buscarTodosLosParkings();
		List<ParkingDTO> dtoList = parkings.stream().map(this::convertToDTO).toList();
		return ResponseEntity.ok(dtoList);
	}

	@PostMapping
	public ResponseEntity<ParkingDTO> createParking(@RequestBody @Valid CrearParkingDTO dto) {
		Parking parking = convertToEntity(dto);
		Parking creado = parkingService.crearParking(parking);
		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(creado));
	}

	// Conversores
	private ParkingDTO convertToDTO(Parking p) {
		ParkingDTO dto = new ParkingDTO();
		dto.setId(p.getId());
		dto.setName(p.getName());
		dto.setCapacity(p.getCapacity());
		return dto;
	}

	private Parking convertToEntity(CrearParkingDTO dto) {
		Parking p = new Parking();
		p.setName(dto.getName());
		p.setCapacity(dto.getCapacity());
		return p;
	}
}
