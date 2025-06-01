package com.xxxiv.dto;

import com.xxxiv.service.RutaPuntoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class VehiculoEnUsoDTO {
    private Integer idVehiculo;
    private String marca;
    private String modelo;
    private List<RutaPuntoDTO> ruta;

    public VehiculoEnUsoDTO(Integer id, String marca, String modelo, List<RutaPuntoDTO> ruta) {
        this.idVehiculo = id;
        this.marca = marca;
        this.modelo = modelo;
        this.ruta = ruta;
    }
}
