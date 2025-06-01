package com.xxxiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Embeddable
public class SeguimientoRutaId implements Serializable {

    @Column(name = "viaje_id")
    private Integer viajeId;

    @Column(name = "punto_index")
    private Integer puntoIndex;

    public SeguimientoRutaId(Integer viajeId, Integer puntoIndex) {
        this.viajeId = viajeId;
        this.puntoIndex = puntoIndex;
    }
}