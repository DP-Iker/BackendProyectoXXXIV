package com.xxxiv.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "seguimiento_ruta")
public class SeguimientoRuta {

    @EmbeddedId
    private SeguimientoRutaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "viaje_id", nullable = false, insertable = false, updatable = false)
    private Viaje viaje;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "velocidad", nullable = false)
    private Double velocidad;


    public SeguimientoRuta(Integer viajeId, Integer puntoIndex, Double latitud, Double longitud, Double velocidad) {
        this.id = new SeguimientoRutaId(viajeId, puntoIndex);
        this.latitud = latitud;
        this.longitud = longitud;
        this.velocidad = velocidad;
    }

    // Getters, Setters
}

