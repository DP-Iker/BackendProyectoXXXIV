package com.xxxiv.dto;

import java.time.LocalDateTime;

import com.xxxiv.model.enums.EstadoReserva;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroReservasDTO {
	private Integer usuarioId;
	private Integer vehiculoId;
    private EstadoReserva estado;
    private LocalDateTime fechaReserva;
}
