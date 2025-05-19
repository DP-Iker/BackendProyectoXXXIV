package com.xxxiv.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.xxxiv.model.enums.Estado;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String marca;

    @Column(nullable = false, length = 45)
    private String modelo;

    @Lob
    private byte[] imagen;

    @Column(nullable = false)
    private int kilometraje;

    private LocalDate ultimaRevision;

    @Column(nullable = false)
    private int autonomia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.DISPONIBLE;

    @Column(nullable = false)
    private double latitud;
    
    @Column(nullable = false)
    private double longitud;
}
