package com.xxxiv.dto;

import java.time.LocalDateTime;

import com.xxxiv.model.Viaje;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViajeDTO {
	private int id;
    private SoloIdDTO usuario;
    private SoloIdDTO reserva;
    private ReservaVehiculoDTO vehiculo;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer kmRecorridos;
    private Double precio;
    
	// Constructor para mapear desde entidad Viaje
    public ViajeDTO(Viaje viaje) {
        this.id = viaje.getId();
        if (viaje.getUsuario() != null) {
            this.usuario = new SoloIdDTO(viaje.getUsuario().getId());
        }
        if (viaje.getReserva() != null) {
            this.reserva = new SoloIdDTO(viaje.getReserva().getId());
        }
        if (viaje.getVehiculo() != null) {
            this.vehiculo = new ReservaVehiculoDTO(viaje.getVehiculo());
        }
        this.fechaInicio = viaje.getFechaInicio();
        this.fechaFin = viaje.getFechaFin();
        this.kmRecorridos = viaje.getKmRecorridos();
        this.precio = viaje.getPrecio();
    }
}
