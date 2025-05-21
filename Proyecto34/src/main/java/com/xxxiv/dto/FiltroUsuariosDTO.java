package com.xxxiv.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FiltroUsuariosDTO {
	private String usuario;
    private String email;
    private Boolean estaBloqueado;
    private Boolean esAdministrador;
    private LocalDateTime createdAt;
}
