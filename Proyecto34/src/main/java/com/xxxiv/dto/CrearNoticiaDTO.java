package com.xxxiv.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearNoticiaDTO {

    private String titulo;
    private String contenido;
    private LocalDateTime fecha;
    private String idiomaCodigo;
    private String usuario;
}
