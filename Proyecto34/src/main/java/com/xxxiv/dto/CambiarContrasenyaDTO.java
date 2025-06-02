package com.xxxiv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CambiarContrasenyaDTO {
	@NotBlank
	@Pattern(regexp = "^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.?[A-Za-z0-9-_.+/=]*$", message="Token con formato inválido")
	@Schema(description = "Token de cambio de contraseña", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30")
	private String token;

	@NotBlank(message = "La contraseña es obligatoria")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "La contraseña no cumple los requisitos de seguridad")
	@Schema(description = "Contraseña (mínimo 8 caracteres, una mayúscula, una minúscula, un número y un símbolo)", example = "Pa$sword123")
	private String contrasenyaNueva;
}
