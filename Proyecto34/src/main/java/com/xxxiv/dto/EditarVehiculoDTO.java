package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditarVehiculoDTO {
	    private String marca;
	    private String modelo;
	    private Integer kilometraje;
	    private LocalDate ultimaRevision;
	    private Integer autonomia;
	    private Estado estado;
	    private Double latitud;
	    private Double longitud;
	    private String localidad;
	    private Puertas puertas;
	    private Tipo tipo;
	    private Boolean esAccesible;
}
