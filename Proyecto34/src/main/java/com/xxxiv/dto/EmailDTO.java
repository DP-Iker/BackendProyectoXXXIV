package com.xxxiv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDTO {
	@NotBlank(message = "El e-mail es obligatorio")
	@Email(message = "Debe ser un E-mail válido")
	@Schema(description = "Correo electrónico", example = "user@example.com")
	private String email;
}
