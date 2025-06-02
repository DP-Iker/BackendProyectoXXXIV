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
import com.xxxiv.dto.ViajeCrearDTO;
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
	private final UsuarioService usuarioService;
	private final VehiculoService vehiculoService;
	private final VehiculoRepository vehiculoRepository;

	@GetMapping
	@Operation(summary = "Devuelve todos los viajes", description = "Devuelve todos los viajes que hay en la BD")
	public List<ViajeMostrarDTO> getAll() {
		return viajeService.findAll().stream().map(ViajeMostrarDTO::fromEntity).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Devuelve el viaje por ID", description = "Devuelve toda la info del viaje por su ID")
	public Optional<Viaje> getById(@PathVariable Integer id) {
		return viajeService.buscarPorId(id);
	}

	@PostMapping
	@Operation(summary = "Crea un viaje", description = "")
	public Viaje create(@Valid @RequestBody ViajeCrearDTO dto) {
		// 1. Buscar usuario por ID
		Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// 2. Buscar vehículo por ID
		Vehiculo vehiculo = vehiculoService.buscarPorId(dto.getVehiculoId())
				.orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

		// 3. Verificar estado actual del vehículo
		if (vehiculo.getEstado() != Estado.DISPONIBLE) {
			throw new RuntimeException("El vehículo no está disponible para un nuevo viaje");
		}

		// 4. Actualizar estado del vehículo
		vehiculo.setEstado(Estado.EN_USO);
		vehiculo.setLatitud(dto.getLatitud());
		vehiculo.setLongitud(dto.getLongitud());
		vehiculo.setLocalidad(dto.getLocalidad());

		vehiculoRepository.save(vehiculo);

		// 5. Convertir DTO a entidad Viaje sin usuario ni vehículo
		Viaje viaje = dto.toEntity();

		// 6. Asignar las entidades usuario y vehículo completas al viaje
		viaje.setUsuario(usuario);
		viaje.setVehiculo(vehiculo);

		// 7. Guardar el viaje en la base de datos y devolverlo
		return viajeService.save(viaje);
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Actualiza parcialmente un viaje", description = "Finalización y ubicación")
	public Viaje actualizarViaje(@PathVariable Integer id, @RequestBody ViajeActualizarDTO dto) {

		Viaje viaje = viajeService.buscarPorId(id).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

		vehiculoService.actualizarUbicacion(viaje.getVehiculo().getId(), dto.getLatitud(), dto.getLongitud());

		if (dto.getFechaFin() != null) {
			viaje.setFechaFin(dto.getFechaFin());
		}
		if (dto.getKmRecorridos() != null) {
			viaje.setKmRecorridos(dto.getKmRecorridos());
		}

		return viajeService.save(viaje);
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
