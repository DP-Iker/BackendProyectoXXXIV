package com.xxxiv.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ViajeMostrarDTO {

	private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer kmRecorridos;
    private Integer usuarioId;
    private Integer vehiculoId;
    
}
