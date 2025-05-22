package com.xxxiv.dto;

import com.xxxiv.model.enums.Localidad;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UbicacionVehiculosDTO {
	private int id;
    private double latitud;
    private double longitud;
    private Localidad localidad;
}
