package com.xxxiv.dto;

import java.time.LocalDateTime;

import com.xxxiv.model.Carnet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarnetSinValidarDTO {
	private int usuarioId;
	private String imagenUrl;
	private LocalDateTime fechaSolicitud;
	
	public CarnetSinValidarDTO(Carnet carnet) {
        this.usuarioId = carnet.getUsuario().getId();
        this.imagenUrl = carnet.getImagenUrl();
        this.fechaSolicitud = carnet.getFechaSolicitud();
    }
}
