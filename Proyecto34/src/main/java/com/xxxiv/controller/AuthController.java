package com.xxxiv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.dto.CambiarContrasenyaDTO;
import com.xxxiv.dto.CrearUsuarioDTO;
import com.xxxiv.dto.EmailDTO;
import com.xxxiv.dto.LoginResponseDTO;
import com.xxxiv.dto.LoginUsuarioDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
@SecurityRequirement(name = "")
public class AuthController {
	
	private final AuthService authService;

	// POST
	@PostMapping("/login")
	@Operation(summary = "Inicia sesión", description = "Inicia sesión en un usuario si el nombre de usuario existe y la contraseña es la misma")
	public ResponseEntity<?> login(@RequestBody @Valid LoginUsuarioDTO dto) {
		try {
			String token = authService.login(dto.getUsuario(), dto.getContrasenya());

			return ResponseEntity.ok(new LoginResponseDTO(token));

			// Si no consigue hacer
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}

	@PostMapping("/registro")
	@Operation(summary = "Crea un usuario", description = "Crea un usuario si le envias un nombre de usuario único, una contraseña y un email único")
	public ResponseEntity<Usuario> crearUsuario(@RequestBody @Valid CrearUsuarioDTO dto) {
		Usuario usuario = authService.crearUsuario(dto.getUsuario(), dto.getContrasenya(), dto.getEmail());
		return new ResponseEntity<>(usuario, HttpStatus.CREATED);
	}
	
	@PostMapping("/contrasenya-olvidada")
	@Operation(summary = "Envia un correo al email", description = "Envia un correo al email si este existe para cambiar la contraseña")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid EmailDTO dto) {
        authService.enviarCorreoCambioContrasenya(dto.getEmail());
        return ResponseEntity.ok("Correo enviado si el email existe");
    }

    @PostMapping("/cambiar-contrasenya")
    @Operation(summary = "Cambia la contraseña", description = "Cambia la contraseña del usuario según el token")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid CambiarContrasenyaDTO dto) {
        authService.cambiarContrasenya(dto.getToken(), dto.getContrasenyaNueva());
        return ResponseEntity.ok("Contraseña cambiada correctamente");
    }
}
