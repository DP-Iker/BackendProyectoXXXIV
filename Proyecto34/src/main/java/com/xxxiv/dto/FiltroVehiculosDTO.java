package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroVehiculosDTO {
	private String marca;
	private Integer kilometraje;
	private LocalDate ultimaRevision;
	private Integer autonomia;
	private Estado estado;
	private String localidad;
	private Boolean esAccesible;
	private Puertas puertas;
	private Tipo tipo;
}
