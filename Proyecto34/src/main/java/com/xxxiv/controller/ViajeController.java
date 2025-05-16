package com.xxxiv.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.model.Viaje;
import com.xxxiv.service.ViajeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/viajes")
public class ViajeController {

    private final ViajeService viajeService;

    public ViajeController(ViajeService viajeService) {
        this.viajeService = viajeService;
    }

    @GetMapping
    public List<Viaje> getAll() {
        return viajeService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Viaje> getById(@PathVariable Integer id) {
        return viajeService.findById(id);
    }

    @PostMapping
    public Viaje create(@Valid @RequestBody Viaje viaje) {
        return viajeService.save(viaje);
    }

    @PutMapping("/{id}")
    public Viaje update(@PathVariable Integer id, @RequestBody Viaje viaje) {
        viaje.setId(id);
        return viajeService.save(viaje);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        viajeService.deleteById(id);
    }
}
