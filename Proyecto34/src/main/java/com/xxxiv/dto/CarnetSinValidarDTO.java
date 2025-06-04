package com.xxxiv.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarnetSinValidarDTO {
	private int usuarioId;
	private String imagenUrl;
	private LocalDateTime fechaSolicitud;
}
