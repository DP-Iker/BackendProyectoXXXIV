package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SeguimientoDTO {
    private Integer id;
    private String marca;
    private String modelo;
    private List<double[]> ruta;

    private List<Double> velocidades;
}
