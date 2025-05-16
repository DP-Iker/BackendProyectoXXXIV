package com.xxxiv.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViajeMostrarDTO {

	private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer kmRecorridos;
    private Integer usuarioId;
    private Integer vehiculoId;
    
}
