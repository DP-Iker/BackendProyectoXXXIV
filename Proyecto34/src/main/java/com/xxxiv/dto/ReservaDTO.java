package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.xxxiv.model.Reserva;
import com.xxxiv.model.enums.EstadoReserva;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private int id;
    private SoloIdDTO usuario;
    private ViajeDTO viaje;
    private SoloIdDTO vehiculo;
    private SoloIdDTO parkingRecogida;
    private LocalDateTime fechaReserva;
    private EstadoReserva estado;

    // Constructor para mapear desde entidad Reserva
    public ReservaDTO(Reserva reserva) {
        this.id = reserva.getId();
        if (reserva.getUsuario() != null) {
            this.usuario = new SoloIdDTO(reserva.getUsuario().getId());
        }
        if (reserva.getViaje() != null) {
            this.viaje = new ViajeDTO(reserva.getViaje());
        }
        if (reserva.getVehiculo() != null) {
            this.vehiculo = new SoloIdDTO(reserva.getVehiculo().getId());
        }
        if (reserva.getParkingRecogida() != null) {
            this.parkingRecogida = new SoloIdDTO(reserva.getParkingRecogida().getId());
        }
        this.fechaReserva = reserva.getFechaReserva();
        this.estado = reserva.getEstado();
    }
}
