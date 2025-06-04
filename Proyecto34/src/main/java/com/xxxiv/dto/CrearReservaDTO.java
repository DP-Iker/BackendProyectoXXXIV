package com.xxxiv.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearReservaDTO {

    @NotNull
    private Integer vehiculoId;

    @NotNull
    private Integer parkingRecogidaId;
}
