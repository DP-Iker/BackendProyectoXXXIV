package com.xxxiv.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginUsuarioDTO {
	@NotNull
	private String usuario;
	
	@NotNull
	private String contrasenya;
}
