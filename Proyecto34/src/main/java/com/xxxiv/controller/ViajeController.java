package com.xxxiv.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.dto.ViajeActualizarDTO;
import com.xxxiv.dto.CrearViajeDTO;
import com.xxxiv.dto.ViajeMostrarDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.repository.VehiculoRepository;
import com.xxxiv.service.UsuarioService;
import com.xxxiv.service.VehiculoService;
import com.xxxiv.service.ViajeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/viajes")
@SecurityRequirement(name = "bearerAuth")
public class ViajeController {

	private final ViajeService viajeService;
	
	// GET
	@GetMapping
	@Operation(summary = "Devuelve todos los viajes", description = "Devuelve todos los viajes que hay en la BD")
	public List<ViajeMostrarDTO> getAll() {
		return viajeService.findAll().stream().map(ViajeMostrarDTO::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ViajeMostrarDTO> getById(@PathVariable Integer id) {
	    Viaje viaje = viajeService.obtenerViajePorId(id);
	    ViajeMostrarDTO dto = ViajeMostrarDTO.fromEntity(viaje);
	    
	    return ResponseEntity.ok(dto);
	}

	
	// POST
	@PostMapping
	@Operation(summary = "Crea un viaje", description = "Crea un viaje en la BD")
	public ResponseEntity<ViajeMostrarDTO> create(@Valid @RequestBody CrearViajeDTO dto) {
	    Viaje viaje = viajeService.crearViaje(dto);
	    return ResponseEntity.ok(ViajeMostrarDTO.fromEntity(viaje));
	}


	// PATCH
	@PatchMapping("/{id}")
	@Operation(summary = "Actualiza parcialmente un viaje", description = "Finalización y ubicación")
	public ResponseEntity<ViajeMostrarDTO> actualizarViaje(@PathVariable Integer id, @RequestBody ViajeActualizarDTO dto) {
	    Viaje viajeActualizado = viajeService.actualizarParcialmente(id, dto);
	    return ResponseEntity.ok(ViajeMostrarDTO.fromEntity(viajeActualizado));
	}


	@PatchMapping("/{id}/finalizar")
	public ResponseEntity<ViajeMostrarDTO> finalizarViaje(@PathVariable Integer id,
			@RequestBody ViajeActualizarDTO dto) {

		Viaje viajeFinalizado = viajeService.finalizarViaje(id, dto.getFechaFin(), dto.getKmRecorridos());

		return ResponseEntity.ok(ViajeMostrarDTO.fromEntity(viajeFinalizado));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Devuelve todos los vehículos", description = "Devuelve todos los vehículos que hay en la BD")
	public Viaje update(@PathVariable Integer id, @RequestBody Viaje viaje) {
		viaje.setId(id);
		return viajeService.save(viaje);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Borra el viaje", description = "Elimina el viaje de la BD por ID")
	public void delete(@PathVariable Integer id) {
		viajeService.deleteById(id);
	}
}
