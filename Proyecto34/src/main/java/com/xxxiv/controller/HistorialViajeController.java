package com.xxxiv.controller;

import com.xxxiv.dto.HistorialViajeDTO;
import com.xxxiv.service.HistorialViajeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historial")
public class HistorialViajeController {

    private final HistorialViajeService service;

    public HistorialViajeController(HistorialViajeService service) {
        this.service = service;
    }

    @GetMapping("/usuario/{nombreUsuario}")
    public ResponseEntity<List<HistorialViajeDTO>> getHistorialUsuario(
            @PathVariable String nombreUsuario
    ) {
        List<HistorialViajeDTO> historial = service.obtenerHistorialPorUsuario(nombreUsuario);
        return ResponseEntity.ok(historial);
    }
}
