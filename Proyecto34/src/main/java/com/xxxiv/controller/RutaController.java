package com.xxxiv.controller;

import com.xxxiv.dto.RutaDTO;
import com.xxxiv.service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    @GetMapping("/viaje/{viajeId}")
    public ResponseEntity<RutaDTO> obtenerRuta(@PathVariable("viajeId") Integer viajeId) {
        RutaDTO dto = rutaService.obtenerRutaPorViaje(viajeId);
        return ResponseEntity.ok(dto);
    }


    @PutMapping("/viaje/{viajeId}")
    public ResponseEntity<RutaDTO> reemplazarRuta(
            @PathVariable("viajeId") Integer viajeId,
            @Valid @RequestBody RutaDTO rutaDTO) {

        // Solo tomamos rutaDTO.getPuntos() para reemplazar la ruta interna del viaje
        RutaDTO dtoActualizada = rutaService.reemplazarRuta(viajeId, rutaDTO.getPuntos());
        return ResponseEntity.ok(dtoActualizada);
    }
}
