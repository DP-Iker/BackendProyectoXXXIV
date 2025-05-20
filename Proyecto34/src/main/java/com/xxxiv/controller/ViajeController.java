package com.xxxiv.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.dto.ViajeCrearDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.service.UsuarioService;
import com.xxxiv.service.VehiculoService;
import com.xxxiv.service.ViajeService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

	private final ViajeService viajeService;
	private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;

    public ViajeController(ViajeService viajeService, UsuarioService usuarioService, VehiculoService vehiculoService) {
        this.viajeService = viajeService;
        this.usuarioService = usuarioService;
        this.vehiculoService = vehiculoService;
    }

	@GetMapping
	@Operation(summary = "Devuelve todos los viajes", description = "Devuelve todos los viajes que hay en la BD")
	public List<Viaje> getAll() {
		return viajeService.findAll();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Devuelve el viaje por ID", description = "Devuelve toda la info del viaje por su ID")
	public Optional<Viaje> getById(@PathVariable Integer id) {
		return viajeService.findById(id);
	}

	@PostMapping
	@Operation(summary = "Crea un viaje", description = "")
	public Viaje create(@Valid @RequestBody ViajeCrearDTO dto) {
	    // 1. Buscar usuario por ID
	    Usuario usuario = usuarioService.findById(dto.getUsuarioId())
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    // 2. Buscar vehículo por ID
	    Vehiculo vehiculo = vehiculoService.findById(dto.getVehiculoId())
	            .orElseThrow(() -> new RuntimeException("Vehiculo no encontrado"));

	    // 3. Convertir DTO a entidad Viaje sin usuario ni vehículo
	    Viaje viaje = dto.toEntity();

	    // 4. Asignar las entidades usuario y vehículo completas al viaje
	    viaje.setUsuario(usuario);
	    viaje.setVehiculo(vehiculo);

	    // 5. Guardar el viaje en la base de datos y devolverlo
	    return viajeService.save(viaje);
	}


//	@PutMapping("/{id}")
//	@Operation(summary = "Devuelve todos los vehículos", description = "Devuelve todos los vehículos que hay en la BD")
//	public Viaje update(@PathVariable Integer id, @RequestBody Viaje viaje) {
//		viaje.setId(id);
//		return viajeService.save(viaje);
//	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Borra el viaje", description = "Elimina el viaje de la BD por ID")
	public void delete(@PathVariable Integer id) {
		viajeService.deleteById(id);
	}
}
