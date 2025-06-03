package com.xxxiv.controller;

import com.xxxiv.dto.FiltroReservasDTO;
import com.xxxiv.dto.ReservaDTO;
import com.xxxiv.dto.ReservaDetalleDTO;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.enums.EstadoReserva;
import com.xxxiv.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private Pageable normalize(Pageable p) {
	    int max = 50;
	    int size = Math.min(p.getPageSize(), max);
	    return PageRequest.of(p.getPageNumber(), size, p.getSort());
	}

    // GET
    @SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	@Operation(summary = "Devuelve todas las reservas", description = "Devuelve todas los reservas que hay en la BD")
	@Parameters({ 
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") 
	})
    public ResponseEntity<Page<ReservaDTO>> getTodosVehiculos(
	        @RequestParam(required = false) Integer usuarioId,
	        @RequestParam(required = false) Integer vehiculoId,
	        @RequestParam(required = false) EstadoReserva estado, 
	        @RequestParam(required = false) LocalDateTime fechaReserva,
	        Pageable pageable) 
	{
	    Pageable safePageable = normalize(pageable);

	    FiltroReservasDTO filtro = new FiltroReservasDTO();
	    filtro.setUsuarioId(usuarioId);
	    filtro.setVehiculoId(vehiculoId);
	    filtro.setEstado(estado);
	    filtro.setFechaReserva(fechaReserva);

	    Page<Reserva> reservas = reservaService.buscarReservas(filtro, safePageable);
	    Page<ReservaDTO> reservasDTO = reservas.map(ReservaDTO::new);
	    
	    return ResponseEntity.ok(reservasDTO);
	}


    @PostMapping
    public ResponseEntity<ReservaDetalleDTO> crearReserva(@RequestBody ReservaDetalleDTO dto) {
        return ResponseEntity.status(201).build();
    }
}
