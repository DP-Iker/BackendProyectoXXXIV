package com.xxxiv.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViajeCrearDTO {
	
	    @NotNull(message = "La fecha de inicio es obligatoria")
	    @PastOrPresent(message = "La fecha de inicio no puede ser futura")
	    private LocalDate fechaInicio;

	    @PastOrPresent(message = "La fecha de fin no puede ser futura")
	    private LocalDate fechaFin;

	    @Min(value = 0, message = "Los kilómetros recorridos no pueden ser negativos")
	    private Integer kmRecorridos;

	    @NotNull(message = "El id del usuario es obligatorio")
	    private Integer usuarioId;

	    @NotNull(message = "El id del vehículo es obligatorio")
	    private Integer vehiculoId;
}
