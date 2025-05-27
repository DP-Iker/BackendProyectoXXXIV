package com.xxxiv.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.dto.LoginResponseDTO;
import com.xxxiv.dto.LoginUsuarioDTO;
import com.xxxiv.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUsuarioDTO dto) {
        try {
            String token = authService.login(dto.getUsuario(), dto.getContrasenya());

            return ResponseEntity.ok(new LoginResponseDTO(token));
            
            // Si no consigue hacer 
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
