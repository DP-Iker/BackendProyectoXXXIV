package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditarVehiculoDTO {
    @Size(max = 30, message = "La marca no puede superar 30 caracteres")
    private String marca;

    @Size(max = 50, message = "El modelo no puede superar 50 caracteres")
    private String modelo;

    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    private LocalDate ultimaRevision;

    @Min(value = 0, message = "La autonomía no puede ser negativa")
    private Integer autonomia;

    private Estado estado;

    @DecimalMin(value = "-90.0", message = "Latitud fuera de rango")
    @DecimalMax(value = "90.0", message = "Latitud fuera de rango")
    private Double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud fuera de rango")
    @DecimalMax(value = "180.0", message = "Longitud fuera de rango")
    private Double longitud;

    @Size(max = 50, message = "La localidad no puede superar 50 caracteres")
    private String localidad;

    private Puertas puertas;
    private Tipo tipo;

    private Boolean esAccesible;
}
