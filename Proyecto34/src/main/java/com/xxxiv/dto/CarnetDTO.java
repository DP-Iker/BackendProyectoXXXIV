package com.xxxiv.dto;

import java.time.LocalDate;

import com.xxxiv.model.enums.EstadoCarnet;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarnetDTO {
    private Integer id;
    private String usuario;
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private LocalDate fechaEmision;
    private LocalDate fechaCaducidad;
    private EstadoCarnet estado;
    private String imagenUrl;
}
