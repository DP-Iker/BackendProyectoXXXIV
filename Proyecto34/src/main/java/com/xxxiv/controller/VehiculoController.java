package com.xxxiv.controller;

import com.xxxiv.dto.UbicacionVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.service.VehiculoService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {
	@Autowired
    VehiculoService vehiculoService;

    // Obtener todos los vehículos
    @GetMapping
    @Operation(summary = "Devuelve todos los vehículos", description = "Devuelve todos los vehículos que hay en la BD")
    public List<Vehiculo> getAllVehiculos() {
        return vehiculoService.findAll();
    }

    // Obtener vehículo por ID
    @GetMapping("/{id}")
    @Operation(summary = "Devuelve el vehículo por ID", description = "Devuelve todos los datos del vehículo según su ID")
    public ResponseEntity<Vehiculo> getVehiculoById(@PathVariable int id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtiene la ubicacion de los vehiculos disponibles
    @GetMapping("/ubicaciones")
    @Operation(summary = "Obtiene la ubicación de vehículos disponibles", description = "Devuelve la latitud y longitud de todos los vehículos con estado DISPONIBLE")
    public List<UbicacionVehiculosDTO> getUbicacion() {
        return vehiculoService.getUbicaciones();
    }
}
