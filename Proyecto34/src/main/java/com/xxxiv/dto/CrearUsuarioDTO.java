package com.xxxiv.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CrearUsuarioDTO {
	@NotNull(message = "El nombre de usuario es obligatorio")
	@Size(min = 3, max = 25, message = "El usuario debe tener entre 3 y 25 caracteres")
	private String usuario;
	
	@NotNull(message = "La contraseña es obligatoria")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "La contraseña no cumple los requisitos de seguridad")
	private String contrasenya;
	
	@NotNull(message = "El e-mail es obligatorio")
	@Email(message = "Debe ser un E-mail válido")
	private String email;
}
