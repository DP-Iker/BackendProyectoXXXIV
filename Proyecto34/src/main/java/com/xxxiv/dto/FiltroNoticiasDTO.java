package com.xxxiv.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class FiltroNoticiasDTO {
    private String titulo;
    private Integer usuarioId;
    private String idiomaCodigo;
    private LocalDateTime createdAt;
}
