package com.xxxiv.controller;

import com.xxxiv.dto.ReservaDetalleDTO;
import com.xxxiv.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    @GetMapping("/usuario/{nombreUsuario}")
    public ResponseEntity<List<ReservaDetalleDTO>> getReservasPorUsuario(
            @PathVariable String nombreUsuario
    ) {
        List<ReservaDetalleDTO> lista = reservaService.obtenerReservasPorUsuario(nombreUsuario);
        return ResponseEntity.ok(lista);
    }


    @PostMapping
    public ResponseEntity<ReservaDetalleDTO> crearReserva(@RequestBody ReservaDetalleDTO dto) {
        return ResponseEntity.status(201).build();
    }
}
