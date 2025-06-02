package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class CrearVehiculoDTO {

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 30)
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50)
    private String modelo;

    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private int kilometraje;

    @NotNull(message = "La fecha de última revisión es obligatoria")
    private LocalDate ultimaRevision;

    @Min(value = 0, message = "La autonomía no puede ser negativa")
    private int autonomia;

    private Estado estado;

    @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
    private double latitud;

    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    private double longitud;

    @NotBlank(message = "La localidad es obligatoria")
    @Size(max = 50)
    private String localidad;

    @NotNull(message = "El número de puertas es obligatorio")
    private Puertas puertas;

    @NotNull(message = "El tipo de vehículo es obligatorio")
    private Tipo tipo;

    private boolean esAccesible = false;
}
