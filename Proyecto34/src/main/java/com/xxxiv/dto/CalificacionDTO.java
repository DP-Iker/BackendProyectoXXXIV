package com.xxxiv.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDTO {
    private Integer id;
    private Integer vehiculoId;
    private String contenido;
    private Integer calificacion;
    private LocalDateTime createdAt;

    private String username;
    private String avatar;
}
