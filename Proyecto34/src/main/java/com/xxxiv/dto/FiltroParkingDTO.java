package com.xxxiv.dto;

import lombok.Data;

@Data
public class FiltroParkingDTO {
    private String nombre;      // Filtrar por nombre (puede ser parcial)
    private Integer capacidadMinima;  // Filtrar parkings con capacidad >=   
}
