package com.xxxiv.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

	@Test
	void testTokenGeneracionYValidacion() {
	    JwtUtil jwtUtil = new JwtUtil();

	    // Simulamos lo que Spring hace
	    jwtUtil.secretKeyEnv = java.util.Base64.getEncoder()
	        .encodeToString("clave-super-secreta-12345678901234567890".getBytes());
	    jwtUtil.expirationMs = 3600000; // 1 hora
	    jwtUtil.init();

	    String token = jwtUtil.generarToken("usuarioPrueba", true);
	    assertNotNull(token);

	    Claims claims = jwtUtil.validarToken(token);
	    assertEquals("usuarioPrueba", claims.getSubject());
	}
}
