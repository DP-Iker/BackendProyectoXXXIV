package com.xxxiv.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "carnet")
public class Carnet {

    @Id
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @MapsId  // Indica que usa el mismo valor de PK que el usuario
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 9, unique = true, nullable = false)
    private String dni;

    @Column(length = 45)
    private String nombre;

    @Column(length = 45)
    private String apellido;

    private LocalDate fechaNacimiento;

    private LocalDate fechaEmision;

    private LocalDate fechaCaducidad;
}
