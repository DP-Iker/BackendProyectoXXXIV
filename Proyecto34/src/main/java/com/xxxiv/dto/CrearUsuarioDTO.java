package com.xxxiv.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearUsuarioDTO {
	@NotNull
	private String usuario;
	
	@NotNull
	private String contrasenya;
	
	@NotNull
	@Email
	private String email;
}
