package com.xxxiv.controller;

import com.xxxiv.dto.CalificacionDTO;
import com.xxxiv.model.Calificacion;
import com.xxxiv.service.CalificacionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {
    @Autowired
    CalificacionService calificacionService;

    @PostMapping
    public ResponseEntity<Calificacion> crearCalificacion(@Valid @RequestBody Calificacion calificacion) {
        Calificacion creada = calificacionService.crear(calificacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<Calificacion>> listarCalificaciones() {
        List<Calificacion> todas = calificacionService.buscarTodas();
        return ResponseEntity.ok(todas);
    }

    @GetMapping("/vehiculo/{id}")
    public ResponseEntity<List<CalificacionDTO>> obtenerPorIdVehiculo(@PathVariable Integer id) {
        List<CalificacionDTO> dtos = calificacionService.buscarPorIdVehiculo(id);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Calificacion> obtenerPorId(@PathVariable Integer id) {
        return calificacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Calificación no encontrada con id=" + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Calificacion> actualizarCalificacion(
            @PathVariable Integer id,
            @Valid @RequestBody Calificacion calificacion) {
        Calificacion actualizada = calificacionService.actualizar(id, calificacion);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCalificacion(@PathVariable Integer id) {
        calificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
