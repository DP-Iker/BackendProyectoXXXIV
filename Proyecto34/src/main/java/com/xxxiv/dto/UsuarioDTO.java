package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioDTO {
	private String username;
    private String email;
    private String fotoUrl;
}
