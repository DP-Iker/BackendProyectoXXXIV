package com.xxxiv.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.ViajeDTO;
import com.xxxiv.dto.FiltroViajesDTO;
import com.xxxiv.dto.FinalizarViajeDTO;
import com.xxxiv.dto.ViajeResumenDTO;
import com.xxxiv.model.Viaje;
import com.xxxiv.service.UsuarioService;
import com.xxxiv.service.ViajeService;
import com.xxxiv.util.PageableNormalizer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
	private final PageableNormalizer pageableNormalizer;
	
	// GET
	@GetMapping("/admin")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Devuelve todos los viajes", description = "Devuelve todos los viajes que hay en la BD")
	@Parameters({ 
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") 
	})
	public ResponseEntity<Page<ViajeDTO>> getTodasReservas(
	        @RequestParam(required = false) Integer usuarioId,
	        @RequestParam(required = false) Integer vehiculoId,
	        @RequestParam(required = false) LocalDateTime fechaInicio, 
	        @RequestParam(required = false) LocalDateTime fechaFin,
	        @RequestParam(required = false) Integer kmRecorridos,
	        @RequestParam(required = false) Double precio,
	        Pageable pageable) 
	{
	    Pageable safePageable = pageableNormalizer.normalize(pageable);

	    FiltroViajesDTO filtro = new FiltroViajesDTO();
	    filtro.setUsuarioId(usuarioId);
	    filtro.setVehiculoId(vehiculoId);
	    filtro.setFechaInicio(fechaInicio);
	    filtro.setFechaFin(fechaFin);
	    filtro.setKmRecorridos(kmRecorridos);
	    filtro.setPrecio(precio);

	    Page<Viaje> viajes = viajeService.buscarViajes(filtro, safePageable);
	    Page<ViajeDTO> viajesDTO = viajes.map(ViajeDTO::new);
	    
	    return ResponseEntity.ok(viajesDTO);
	}
	
	@GetMapping
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Devuelve todos los viajes propios", description = "Devuelve todos los viajes que el usuario tiene vinculados")
	@Parameters({ 
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") 
	})
	public ResponseEntity<Page<ViajeDTO>> getTodasReservasPropias(Authentication authentication,
	        @RequestParam(required = false) Integer vehiculoId,
	        @RequestParam(required = false) LocalDateTime fechaInicio, 
	        @RequestParam(required = false) LocalDateTime fechaFin,
	        @RequestParam(required = false) Integer kmRecorridos,
	        @RequestParam(required = false) Double precio,
	        Pageable pageable) 
	{
	    Pageable safePageable = pageableNormalizer.normalize(pageable);
	    String username = authentication.getName();
	    int usuarioId = usuarioService.obtenerUsuarioPorNombre(username).getId();

	    FiltroViajesDTO filtro = new FiltroViajesDTO();
	    filtro.setUsuarioId(usuarioId);
	    filtro.setVehiculoId(vehiculoId);
	    filtro.setFechaInicio(fechaInicio);
	    filtro.setFechaFin(fechaFin);
	    filtro.setKmRecorridos(kmRecorridos);
	    filtro.setPrecio(precio);

	    Page<Viaje> viajes = viajeService.buscarViajes(filtro, safePageable);
	    Page<ViajeDTO> viajesDTO = viajes.map(ViajeDTO::new);
	    
	    return ResponseEntity.ok(viajesDTO);
	}

	@GetMapping("/admin/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Busca un viaje", description = "Busca un viaje por ID")
	public ResponseEntity<ViajeDTO> getPorId(@PathVariable Integer id) {
	    Viaje viaje = viajeService.obtenerViajePorId(id);
	    ViajeDTO dto = new ViajeDTO(viaje);
	    
	    return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Busca un viaje propio", description = "Busca un viaje por ID si éste pertenece al usuario")
	public ResponseEntity<ViajeDTO> getPorIdPropio(Authentication authentication, @PathVariable Integer id) {
		String nombreUsuario = authentication.getName();
	    Viaje viaje = viajeService.obtenerViajePropioPorId(nombreUsuario, id);
	    
	    ViajeDTO dto = new ViajeDTO(viaje);
	    
	    return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/resumen/{id}")
	public ResponseEntity<ViajeResumenDTO> getResumenViaje(Authentication authentication, @PathVariable int id) {
		String nombreUsuario = authentication.getName();
	    Viaje viaje = viajeService.obtenerViajePropioPorId(nombreUsuario, id);
	    
	    ViajeResumenDTO resumen = new ViajeResumenDTO(viaje);
	    return ResponseEntity.ok(resumen);
	}



	@PatchMapping("/{id}/finalizar")
	public ResponseEntity<ViajeDTO> finalizarViaje(Authentication authentication, @PathVariable Integer id,
			@RequestBody @Valid FinalizarViajeDTO dto) {

		String nombreUsuario = authentication.getName();
		Viaje viajeFinalizado = viajeService.finalizarViaje(id, nombreUsuario, dto.getKilometraje());
		ViajeDTO viajeFormatado = new ViajeDTO(viajeFinalizado);

		return ResponseEntity.ok(viajeFormatado);
	}

	// DELETE
	@DeleteMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Borra el viaje", description = "Elimina el viaje de la BD por ID")
	public ResponseEntity<Void> eliminarViaje(@PathVariable Integer id) {
		viajeService.eliminarViaje(id);
		
		return ResponseEntity.noContent().build();
	}
}
