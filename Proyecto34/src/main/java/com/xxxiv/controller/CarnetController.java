package com.xxxiv.controller;

import com.xxxiv.model.Carnet;
import com.xxxiv.service.CarnetService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carnets")
@SecurityRequirement(name = "bearerAuth")
public class CarnetController {

	@Autowired
    CarnetService carnetService;

    @GetMapping
    public List<Carnet> getAllCarnets() {
        return carnetService.findAll();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Carnet> getCarnetById(@PathVariable Integer usuarioId) {
        Optional<Carnet> carnetOpt = carnetService.findById(usuarioId);
        return carnetOpt.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
