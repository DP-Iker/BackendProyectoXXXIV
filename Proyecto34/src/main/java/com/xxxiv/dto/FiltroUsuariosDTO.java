package com.xxxiv.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroUsuariosDTO {
	private String usuario;
    private String email;
    private Boolean estaBloqueado;
    private Boolean esAdministrador;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAt;
}
