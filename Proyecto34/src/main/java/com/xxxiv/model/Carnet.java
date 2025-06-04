package com.xxxiv.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 9, unique = true)
    private String dni;

    @Column(length = 50)
    private String nombre;

    @Column(length = 50)
    private String apellido;

    private LocalDate fechaNacimiento;

    private LocalDate fechaEmision;

    private LocalDate fechaCaducidad;

    @Column(length = 100, name = "imagen_url", nullable = false)
    private String imagenUrl;

    @Column(name = "esta_validado", nullable = false)
    private boolean estaValidado = false;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;
}
