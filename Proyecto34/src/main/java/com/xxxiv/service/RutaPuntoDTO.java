package com.xxxiv.service;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RutaPuntoDTO {
    private double latitud;
    private double longitud;
    private double velocidad;

    public RutaPuntoDTO(Double latitud, Double longitud, Double velocidad) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
    }
}
