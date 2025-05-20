package com.xxxiv.controller;

import java.util.List;


import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.model.Caracteristica;
import com.xxxiv.service.CaracteristicaService;

@RestController
@RequestMapping("/caracteristicas")
public class CaracteristicaController {

    private final CaracteristicaService caracteristicaService;

    public CaracteristicaController(CaracteristicaService caracteristicaService) {
        this.caracteristicaService = caracteristicaService;
    }

    @GetMapping
    public List<Caracteristica> listar() {
        return caracteristicaService.obtenerTodas();
    }

}