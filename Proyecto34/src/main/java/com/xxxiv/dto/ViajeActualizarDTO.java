package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.enums.Localidad;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViajeActualizarDTO {
	private Double latitud;
	private Double longitud;
	private LocalDate fechaFin;
	private Integer kmRecorridos;
	private Localidad localidad;
}
