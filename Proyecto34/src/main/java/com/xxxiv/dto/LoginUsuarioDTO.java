package com.xxxiv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginUsuarioDTO {
	@NotBlank(message = "El nombre de usuario es obligatorio")
	@Size(min = 3, max = 25, message = "El usuario debe tener entre 3 y 25 caracteres")
	@Schema(description = "Nombre de usuario", example = "user")
	private String usuario;
	
	@NotBlank(message = "La contraseña es obligatoria")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "La contraseña no cumple los requisitos de seguridad")
	@Schema(description = "Contraseña (mínimo 8 caracteres, una mayúscula, una minúscula, un número y un símbolo)", example = "Pa$sword123")
	private String contrasenya;
}
