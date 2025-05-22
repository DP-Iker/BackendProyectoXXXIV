package com.xxxiv.controller;

import com.xxxiv.dto.FiltroVehiculosDTO;
import com.xxxiv.dto.UbicacionVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Localidad;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;
import com.xxxiv.service.VehiculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    // Obtener todos los vehículos   
    @GetMapping
	@Operation(summary = "Devuelve todos los vehículos", description = "Devuelve todos los vehículos que hay en la BD")
	@Parameters({
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc")
	})
	public ResponseEntity<Page<Vehiculo>> getVehiculos(
			@RequestParam(required = false) String marca,
			@RequestParam(required = false) Integer kilometraje, 
			@RequestParam(required = false) LocalDate ultimaRevision,
			@RequestParam(required = false) Integer autonomia,
			@RequestParam(required = false) Estado estado,
			@RequestParam(required = false) Localidad localidad,
			@RequestParam(required = false) Boolean esAccesible,
			@RequestParam(required = false) Puertas puertas,
			@RequestParam(required = false) Tipo tipo,
			Pageable pageable
	) {
		// Controla que el tamaño no sea excesivo
		int maxPageSize = 50;  // Límite de elementos por página
	    int size = pageable.getPageSize() > maxPageSize ? maxPageSize : pageable.getPageSize();

	    Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
	    
		// Crea el filtro
	    FiltroVehiculosDTO filtro = new FiltroVehiculosDTO();
		filtro.setMarca(marca);
		filtro.setKilometraje(kilometraje);
		filtro.setUltimaRevision(ultimaRevision);
		filtro.setAutonomia(autonomia);
		filtro.setEstado(estado);
		filtro.setLocalidad(localidad);
		filtro.setEsAccesible(esAccesible);
		filtro.setPuertas(puertas);
		filtro.setTipo(tipo);

		Page<Vehiculo> vehiculos = vehiculoService.buscarVehiculos(filtro, safePageable);
		return ResponseEntity.ok(vehiculos);
	}

    // Obtener vehículo por ID
    @GetMapping("/{id}")
    @Operation(summary = "Devuelve el vehículo por ID", description = "Devuelve todos los datos del vehículo según su ID")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable int id) {
        return vehiculoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtiene la ubicacion de los vehiculos disponibles
    @GetMapping("/ubicaciones")
    @Operation(summary = "Obtiene la ubicación de vehículos disponibles", description = "Devuelve la latitud y longitud de todos los vehículos con estado DISPONIBLE")
    public List<UbicacionVehiculosDTO> getUbicacion() {
        return vehiculoService.getUbicaciones();
    }
    
    @GetMapping("/localidades")
    @Operation(summary = "Obtiene todas las localidades dónde hay vehículos disponibles", description = "Devuelve las localidades en la que hay vehículos con estado DISPONIBLE")
    public List<Localidad> getLocalidades() {
        return vehiculoService.getLocalidadesDisponibles();
    }
}
