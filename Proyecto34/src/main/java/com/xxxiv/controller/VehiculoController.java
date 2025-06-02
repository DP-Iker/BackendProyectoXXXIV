package com.xxxiv.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xxxiv.dto.ActualizarUbicacionDTO;
import com.xxxiv.dto.CrearVehiculoDTO;
import com.xxxiv.dto.EditarVehiculoDTO;
import com.xxxiv.dto.FiltroVehiculosDTO;
import com.xxxiv.dto.UbicacionVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;
import com.xxxiv.service.ImgurService;
import com.xxxiv.service.VehiculoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

	private final VehiculoService vehiculoService;
	private final ImgurService imgurService;

	// GET
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	@Operation(summary = "Devuelve todos los vehículos", description = "Devuelve todos los vehículos que hay en la BD")
	@Parameters({ @Parameter(name = "page", description = "Número de página", example = "0"),
			@Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
			@Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") })
	public ResponseEntity<Page<Vehiculo>> getTodosVehiculos(@RequestParam(required = false) String marca,
			@RequestParam(required = false) Integer kilometraje,
			@RequestParam(required = false) LocalDate ultimaRevision, 
			@RequestParam(required = false) Integer autonomia,
			@RequestParam(required = false) Estado estado, 
			@RequestParam(required = false) String localidad,
			@RequestParam(required = false) Boolean esAccesible, 
			@RequestParam(required = false) Puertas puertas,
			@RequestParam(required = false) Tipo tipo, Pageable pageable) {
		int maxPageSize = 50;
		int size = pageable.getPageSize() > maxPageSize ? maxPageSize : pageable.getPageSize();
		Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

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
	
	@GetMapping
	@Operation(summary = "Devuelve todos los vehículos que puedan ver los usuarios", description = "Devuelve todos los vehículos que hay en la BD aptos para que los vean cualquier usuario")
	@Parameters({ @Parameter(name = "page", description = "Número de página", example = "0"),
			@Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
			@Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") })
	public ResponseEntity<Page<Vehiculo>> getVehiculos(@RequestParam(required = false) String marca,
			@RequestParam(required = false) Integer kilometraje,
			@RequestParam(required = false) Integer autonomia,
			@RequestParam(required = false) String localidad,
			@RequestParam(required = false) Boolean esAccesible, 
			@RequestParam(required = false) Puertas puertas,
			@RequestParam(required = false) Tipo tipo, Pageable pageable) {
		int maxPageSize = 50;
		int size = pageable.getPageSize() > maxPageSize ? maxPageSize : pageable.getPageSize();
		Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

		FiltroVehiculosDTO filtro = new FiltroVehiculosDTO();
		filtro.setMarca(marca);
		filtro.setKilometraje(kilometraje);
		filtro.setAutonomia(autonomia);
		filtro.setLocalidad(localidad);
		filtro.setEsAccesible(esAccesible);
		filtro.setPuertas(puertas);
		filtro.setTipo(tipo);

		Page<Vehiculo> vehiculos = vehiculoService.buscarVehiculos(filtro, safePageable);
		return ResponseEntity.ok(vehiculos);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Devuelve el vehículo por ID", description = "Devuelve todos los datos del vehículo según su ID")
	public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable int id) {
	    return vehiculoService.buscarPorId(id)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/ubicaciones")
	@Operation(summary = "Obtiene la ubicación de vehículos disponibles", description = "Devuelve la latitud y longitud de todos los vehículos con estado DISPONIBLE, se puede indicar el tipo opcionalmente")
	public ResponseEntity<List<UbicacionVehiculosDTO>> getUbicacion(@RequestParam(required = false) Tipo tipo) {
		List<UbicacionVehiculosDTO> respuesta = vehiculoService.getUbicaciones(tipo);
	
		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/localidades")
	@Operation(summary = "Obtiene todas las localidades dónde hay vehículos disponibles", description = "Devuelve las localidades en la que hay vehículos con estado DISPONIBLE")
	public ResponseEntity<List<String>> getLocalidades() {
		List<String> respuesta = vehiculoService.getLocalidadesDisponibles();
		
		return ResponseEntity.ok(respuesta);
	}
	
	@GetMapping("/marcas")
	@Operation(summary = "Obtiene todas las marcas de los vehículos disponibles", description = "Devuelve las marcas de los vehículos con estado DISPONIBLE")
	public ResponseEntity<List<String>> getMarcas() {
		List<String> respuesta = vehiculoService.getMarcasDisponibles();
		
		return ResponseEntity.ok(respuesta);
	}
	
	// POST
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Crea un nuevo vehículo", description = "Crea un vehículo con los datos proporcionados (solo accesible por administradores)")
	public ResponseEntity<Vehiculo> crearVehiculo(
			@RequestPart("vehiculo") @Valid CrearVehiculoDTO dto,
		    @RequestPart("imagen") MultipartFile imagen) throws IOException {
		String imagenUrl = imgurService.subirImagen(imagen.getBytes());
	    Vehiculo vehiculoCreado = vehiculoService.crearVehiculo(dto, imagenUrl);
	    
	    return new ResponseEntity<>(vehiculoCreado, HttpStatus.CREATED);
	}


	// PATCH
	@PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Edita un vehículo")
	public ResponseEntity<?> editarVehiculo(
	        @PathVariable int id,
	        @RequestPart(name = "vehiculo", required = false) EditarVehiculoDTO dto,
	        @RequestPart(name = "imagen", required = false) MultipartFile imagen) throws IOException {

		String imagenUrl = imgurService.subirImagen(imagen.getBytes());
	    vehiculoService.editarVehiculo(id, dto, imagenUrl);
	    return ResponseEntity.noContent().build();
	}

	
	@PatchMapping("{id}/ubicacion")
	@Operation(summary = "Actualiza la ubicación de un vehículo", description = "Modifica la latitud y longitud de un vehículo en base a su ID")
	public ResponseEntity<String> actualizarUbicacion(@PathVariable int id, @RequestBody @Valid ActualizarUbicacionDTO dto) {
		String respuesta = vehiculoService.actualizarUbicacion(id, dto.getLatitud(), dto.getLongitud());
		
		return ResponseEntity.ok(respuesta);
	}
}
