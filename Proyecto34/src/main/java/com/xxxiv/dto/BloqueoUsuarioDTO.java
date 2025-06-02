package com.xxxiv.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BloqueoUsuarioDTO {
	@NotBlank
	@Schema(description = "Mensaje de bloqueo", example = "Violación de las normas")
	String mensaje;
}
