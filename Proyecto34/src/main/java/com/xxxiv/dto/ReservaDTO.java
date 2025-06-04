package com.xxxiv.dto;

import java.time.LocalDateTime;

import com.xxxiv.model.Reserva;
import com.xxxiv.model.enums.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private int id;
    private SoloIdDTO usuario;
    private SoloIdDTO viaje;
    private ReservaVehiculoDTO vehiculo;
    private ReservaParkingDTO parkingRecogida;
    private LocalDateTime fechaReserva;
    private EstadoReserva estado;

    // Constructor para mapear desde entidad Reserva
    public ReservaDTO(Reserva reserva) {
        this.id = reserva.getId();
        if (reserva.getUsuario() != null) {
            this.usuario = new SoloIdDTO(reserva.getUsuario().getId());
        }
        if (reserva.getViaje() != null) {
            this.viaje = new SoloIdDTO(reserva.getViaje().getId());
        }
        if (reserva.getVehiculo() != null) {
            this.vehiculo = new ReservaVehiculoDTO(reserva.getVehiculo());
        }
        if (reserva.getParkingRecogida() != null) {
            this.parkingRecogida = new ReservaParkingDTO(reserva.getParkingRecogida());
        }
        this.fechaReserva = reserva.getFechaReserva();
        this.estado = reserva.getEstado();
    }
}
