package com.xxxiv.dto;

import com.xxxiv.model.Vehiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaVehiculoDTO {
    private Integer id;
    private String marca;
    private String modelo;

    public ReservaVehiculoDTO(Vehiculo v) {
        this.id = v.getId();
        this.marca = v.getMarca();
        this.modelo = v.getModelo();
    }
}