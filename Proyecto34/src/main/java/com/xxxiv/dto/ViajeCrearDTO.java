package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.Viaje;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	    
	    @NotNull(message = "La longitud es obligatoria para la ubicación")
	    private Double longitud;
	    
	    @NotNull(message = "La latitud es obligatoria para la ubicación")
	    private Double latitud;
	    
	    @NotNull(message = "La localidad es obligatoria")
	    private String localidad;
	    
	    
	    //Mapea el DTO a entidad
	    public Viaje toEntity() {
	        Viaje viaje = new Viaje();
	        viaje.setFechaInicio(this.fechaInicio);
	        viaje.setFechaFin(this.fechaFin);
	        viaje.setKmRecorridos(this.kmRecorridos);
	        

	        return viaje;
	    }

}
