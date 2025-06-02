package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.Viaje;

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
	private Double longitud;
	private Double latitud;
	private Double precio;

	public static ViajeMostrarDTO fromEntity(Viaje viaje) {
		ViajeMostrarDTO dto = new ViajeMostrarDTO();
		dto.setId(viaje.getId());
		dto.setFechaInicio(viaje.getFechaInicio());
		dto.setFechaFin(viaje.getFechaFin());
		dto.setKmRecorridos(viaje.getKmRecorridos());

		if (viaje.getUsuario() != null) {
			dto.setUsuarioId(viaje.getUsuario().getId());
		}

		if (viaje.getVehiculo() != null) {
			dto.setVehiculoId(viaje.getVehiculo().getId());
		}
		dto.setLatitud(viaje.getLatitud());
		dto.setLongitud(viaje.getLongitud());
		dto.setPrecio(viaje.getPrecio());

		return dto;
	}
}
