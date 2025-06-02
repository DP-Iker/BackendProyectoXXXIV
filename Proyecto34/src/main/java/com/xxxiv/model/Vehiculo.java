package com.xxxiv.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 30)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(length = 100)
    private String imagen;

    @Column(nullable = false)
    private int kilometraje;

    @Column(nullable = false)
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
    
    @Column(nullable = false, length = 50)
    private String localidad;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Puertas puertas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @Column(nullable = false)
    private boolean esAccesible = false;
}
