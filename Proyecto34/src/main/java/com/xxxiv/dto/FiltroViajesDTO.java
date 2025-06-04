package com.xxxiv.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroViajesDTO {
	private Integer usuarioId;
	private Integer vehiculoId;
	private LocalDateTime fechaInicio;
	private LocalDateTime fechaFin;
	private Integer kmRecorridos;
	private Double precio;
}
