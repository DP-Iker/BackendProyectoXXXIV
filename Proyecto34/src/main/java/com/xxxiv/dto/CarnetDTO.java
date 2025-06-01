package com.xxxiv.dto;

import com.xxxiv.model.enums.EstadoCarnet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
