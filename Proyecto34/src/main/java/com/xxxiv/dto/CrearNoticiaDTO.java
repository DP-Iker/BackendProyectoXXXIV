package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CrearNoticiaDTO {

    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private String idiomaCodigo;
    private String usuario;
}
