package com.xxxiv.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroVehiculosDTO {
    private String marca;

    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ultimaRevision;

    @Min(value = 0, message = "La autonomía no puede ser negativa")
    private Integer autonomia;

    private Estado estado;
    private String localidad;
    private Boolean esAccesible;
    private Puertas puertas;
    private Tipo tipo;
}
