package com.xxxiv.controller;

import com.xxxiv.model.Vehiculo;
import com.xxxiv.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
	@Autowired
    VehiculoService vehiculoService;

    // Obtener todos los vehículos
    @GetMapping
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoService.findAll();
    }

    // Obtener vehículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable int id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
