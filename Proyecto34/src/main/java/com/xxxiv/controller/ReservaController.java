package com.xxxiv.controller;

import com.xxxiv.dto.CrearReservaDTO;
import com.xxxiv.dto.FiltroReservasDTO;
import com.xxxiv.dto.ReservaDTO;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.enums.EstadoReserva;
import com.xxxiv.service.ReservaService;
import com.xxxiv.service.UsuarioService;
import com.xxxiv.util.PageableNormalizer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final PageableNormalizer pageableNormalizer;

    // GET
	@GetMapping("/admin")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Devuelve todas las reservas", description = "Devuelve todas las reservas que hay en la BD")
	@Parameters({ 
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") 
	})
    public ResponseEntity<Page<ReservaDTO>> getTodasReservas(
	        @RequestParam(required = false) Integer usuarioId,
	        @RequestParam(required = false) Integer vehiculoId,
	        @RequestParam(required = false) EstadoReserva estado, 
	        @RequestParam(required = false) LocalDateTime fechaReserva,
	        Pageable pageable) 
	{
	    Pageable safePageable = pageableNormalizer.normalize(pageable);

	    FiltroReservasDTO filtro = new FiltroReservasDTO();
	    filtro.setUsuarioId(usuarioId);
	    filtro.setVehiculoId(vehiculoId);
	    filtro.setEstado(estado);
	    filtro.setFechaReserva(fechaReserva);

	    Page<Reserva> reservas = reservaService.buscarReservas(filtro, safePageable);
	    Page<ReservaDTO> reservasDTO = reservas.map(ReservaDTO::new);
	    
	    return ResponseEntity.ok(reservasDTO);
	}
    
    @SecurityRequirement(name = "bearerAuth")
	@GetMapping
	@Operation(summary = "Devuelve todas las reservas del usuario", description = "Devuelve todas los reservas que hay en la BD del usuario")
	@Parameters({ 
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") 
	})
    public ResponseEntity<Page<ReservaDTO>> getReservasPropias(Authentication authentication,
	        @RequestParam(required = false) Integer vehiculoId,
	        @RequestParam(required = false) EstadoReserva estado, 
	        @RequestParam(required = false) LocalDateTime fechaReserva,
	        Pageable pageable) 
	{
    	Pageable safePageable = pageableNormalizer.normalize(pageable);
	    // Consigue el id del usuario con el token
	    String username = authentication.getName();
	    int usuarioId = usuarioService.obtenerUsuarioPorNombre(username).getId();

	    FiltroReservasDTO filtro = new FiltroReservasDTO();
	    filtro.setUsuarioId(usuarioId);
	    filtro.setVehiculoId(vehiculoId);
	    filtro.setEstado(estado);
	    filtro.setFechaReserva(fechaReserva);

	    Page<Reserva> reservas = reservaService.buscarReservas(filtro, safePageable);
	    Page<ReservaDTO> reservasDTO = reservas.map(ReservaDTO::new);
	    
	    return ResponseEntity.ok(reservasDTO);
	}


    // POST
    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Crea una reserva", description = "Crea una reserva en estado PENDIENTE y marca el vehículo como RESERVADO")
    public ResponseEntity<ReservaDTO> crearReserva(Authentication authentication, @RequestBody @Valid CrearReservaDTO dto) {
    	String nombreUsuario = authentication.getName();
        Reserva reservaEntidad = reservaService.crearReserva(nombreUsuario, dto);

        ReservaDTO reservaDTO = new ReservaDTO(reservaEntidad);
        return new ResponseEntity<>(reservaDTO, HttpStatus.CREATED);
    }
    
    // PATCH
    @PatchMapping("/{id}/confirmar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Confirma una reserva", description = "Confirma una reserva si es el usuario que la creó y pone en uso el vehículo")
    public ResponseEntity<Void> confirmarReserva(Authentication authentication, @PathVariable int id) {
    	String nombreUsuario = authentication.getName();
        reservaService.confirmarReserva(nombreUsuario, id);

        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/cancelar")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cancela una reserva propia", description = "Cancela una reserva si es el usuario que la creó y libera el vehículo")
    public ResponseEntity<Void> cancelarReservaPropia(Authentication authentication, @PathVariable int id) {
    	String nombreUsuario = authentication.getName();
        reservaService.cancelarReservaPropia(nombreUsuario, id);

        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/admin/{id}/cancelar")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancela una reserva", description = "Cancela una reserva y libera el vehículo")
    public ResponseEntity<Void> cancelarReserva(@PathVariable int id) {
        reservaService.cancelarReserva(id);

        return ResponseEntity.noContent().build();
    }
    
    // DELETE
    @DeleteMapping("/admin/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Elimina una reserva", description = "Elimina una reserva")
    public ResponseEntity<Void> eliminarReserva(@PathVariable int id) {
        reservaService.eliminarReserva(id);

        return ResponseEntity.noContent().build();
    }
}
