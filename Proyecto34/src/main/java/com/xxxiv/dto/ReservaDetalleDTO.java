package com.xxxiv.dto;

import com.xxxiv.model.enums.EstadoReserva;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservaDetalleDTO {
    private Integer id;
    private Integer usuarioId;
    private Integer viajeId;
    private LocalDate fechaInicioViaje;
    private LocalDate fechaFinViaje;
    private Integer kmRecorridosViaje;
    private Integer vehiculoId;
    private String vehiculoMarca;
    private String vehiculoModelo;
    private Integer parkingRecogidaId;
    private String parkingRecogidaNombre;    // ejemplo, si Parking tiene nombre
    private Integer parkingDevolucionId;
    private String parkingDevolucionNombre;   // ejemplo
    private LocalDateTime fechaReserva;
    private EstadoReserva estado;

    public ReservaDetalleDTO(Integer id, int id1, Integer id2, LocalDate fechaInicio, LocalDate fechaFin, Integer kmRecorridos, Integer id3, String marca, String modelo, Integer id4, String name, Integer id5, String name1, LocalDateTime fechaReserva, EstadoReserva estado) {
        this.id = id;
        this.usuarioId = id1;
        this.viajeId = id2;
        this.fechaInicioViaje = fechaInicio;
        this.fechaFinViaje = fechaFin;
        this.kmRecorridosViaje = kmRecorridos;
        this.vehiculoId = id3;
        this.vehiculoMarca = marca;
        this.vehiculoModelo = modelo;
        this.parkingRecogidaId = id4;
        this.parkingRecogidaNombre = name;
        this.parkingDevolucionId = id5;
        this.parkingDevolucionNombre = name1;
        this.fechaReserva = fechaReserva;
        this.estado = estado;
    }

}
