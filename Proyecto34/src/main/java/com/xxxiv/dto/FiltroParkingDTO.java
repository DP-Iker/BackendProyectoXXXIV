package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroParkingDTO {
    private String name;
    private Integer capacidadMinima;
    private Integer capacidadMaxima;
}
