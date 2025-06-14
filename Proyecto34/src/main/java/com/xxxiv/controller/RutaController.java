package com.xxxiv.controller;

import com.xxxiv.dto.RutaDTO;
import com.xxxiv.service.RutaService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    @GetMapping("/viaje/{viajeId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RutaDTO> obtenerRuta(@PathVariable Integer viajeId) {
        RutaDTO dto = rutaService.obtenerRutaPorViaje(viajeId);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/viaje/{viajeId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RutaDTO> reemplazarRuta(@PathVariable Integer viajeId, @Valid @RequestBody RutaDTO rutaDTO) {

        // Solo tomamos rutaDTO.getPuntos() para reemplazar la ruta interna del viaje
        RutaDTO dtoActualizada = rutaService.reemplazarRuta(viajeId, rutaDTO.getPuntos());
        return ResponseEntity.ok(dtoActualizada);
    }

    @GetMapping("/activas")
    @SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RutaDTO>> obtenerRutasActivas() {
        List<RutaDTO> rutasActivas = rutaService.obtenerRutasActivas();
        return ResponseEntity.ok(rutasActivas);
    }

}
