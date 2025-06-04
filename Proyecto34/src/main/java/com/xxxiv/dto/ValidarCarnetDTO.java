package com.xxxiv.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidarCarnetDTO {

	@NotNull
    @Pattern(regexp = "^[0-9]{8}[A-HJ-NP-TV-Z]$", message = "DNI inválido. Debe tener 8 dígitos seguidos de una letra válida.")
    private String dni;

    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @NotNull
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    private LocalDate fechaNacimiento;

    @NotNull
    private LocalDate fechaEmision;

    @NotNull
    private LocalDate fechaCaducidad;
}

