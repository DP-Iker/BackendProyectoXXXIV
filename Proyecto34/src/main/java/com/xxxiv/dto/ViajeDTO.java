package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.xxxiv.model.Viaje;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {
    private int id;
    private SoloIdDTO usuario;

    // Constructor para mapear desde entidad Viaje
    public ViajeDTO(Viaje viaje) {
        this.id = viaje.getId();
        if (viaje.getUsuario() != null) {
            this.usuario = new SoloIdDTO(viaje.getUsuario().getId());
        }
    }
}

