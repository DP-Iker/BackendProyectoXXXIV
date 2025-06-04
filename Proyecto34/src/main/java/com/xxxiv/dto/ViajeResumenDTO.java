package com.xxxiv.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xxxiv.model.Coordinate;
import com.xxxiv.model.Viaje;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ViajeResumenDTO {
	private Integer reservaId;
    private String marcaVehiculo;
    private String modeloVehiculo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer kmRecorridos;
    private List<Coordinate> cods;
    private Double precio;

    public ViajeResumenDTO(Viaje viaje) {
        this.reservaId = viaje.getReserva().getId();
        this.marcaVehiculo = viaje.getVehiculo().getMarca();
        this.modeloVehiculo = viaje.getVehiculo().getModelo();
        this.fechaInicio = viaje.getFechaInicio();
        this.fechaFin = viaje.getFechaFin();
        this.kmRecorridos = viaje.getKmRecorridos();
        this.cods = viaje.getCods();
        this.precio = viaje.getPrecio();
    }
}
