package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class HistorialViajeDTO {
    private Integer id;
    private Integer vehiculoId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer kmRecorridos;

    public HistorialViajeDTO(Integer id, Integer integer, LocalDate fechaInicio, LocalDate fechaFin, Integer kmRecorridos) {
        this.id = id;
        this.vehiculoId = integer;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.kmRecorridos = kmRecorridos;
    }
}