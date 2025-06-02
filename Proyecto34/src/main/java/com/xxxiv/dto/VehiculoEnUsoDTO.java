package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO que representa un vehículo en uso (viaje activo) junto con su ruta.
 */
@Data
@NoArgsConstructor
public class VehiculoEnUsoDTO {
    /**
     * ID del viaje (o del vehículo, según convenga)
     */
    private Integer id;

    /**
     * Marca del vehículo
     */
    private String marca;

    /**
     * Modelo del vehículo
     */
    private String modelo;

    /**
     * Lista de puntos de la ruta en curso.
     * Cada elemento es un array [latitud, longitud].
     */
    private List<double[]> puntos;
}
