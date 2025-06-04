package com.xxxiv.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RutaDTO  {
    private Integer id;
    private String marca;
    private String modelo;
    private List<double[]> puntos;
}
