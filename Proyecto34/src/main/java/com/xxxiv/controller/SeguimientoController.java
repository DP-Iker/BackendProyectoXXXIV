package com.xxxiv.controller;

import com.xxxiv.dto.SeguimientoDTO;
import com.xxxiv.model.SeguimientoRuta;
import com.xxxiv.service.SeguimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguimiento")
public class SeguimientoController {

    @Autowired
    private SeguimientoService seguimientoService;

    @PostMapping("/{viajeId}")
    public ResponseEntity<Void> guardarRuta(
            @PathVariable Integer viajeId,
            @RequestBody SeguimientoDTO dto
    ) {
        seguimientoService.guardarRuta(viajeId, dto.getRuta(), dto.getVelocidades());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{viajeId}")
    public ResponseEntity<List<SeguimientoRuta>> obtenerRuta(@PathVariable Integer viajeId) {
        return ResponseEntity.ok(seguimientoService.obtenerRutaPorViaje(viajeId));
    }
}
