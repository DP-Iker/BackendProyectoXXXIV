package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class FiltroNoticiasDTO {
    private String titulo;
    private Integer usuarioId;
    private String idiomaCodigo;
    private LocalDateTime createdAt;
}
