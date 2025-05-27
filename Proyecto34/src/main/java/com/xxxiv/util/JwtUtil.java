package com.xxxiv.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

	@Value("${jwt.secretkey}")
	protected String secretKeyEnv;

	@Value("${jwt.expiration-ms:3600000}") // default: 1 hora
	protected long expirationMs;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		// Si tu clave está codificada en Base64 (recomendado en producción)
		try {
			this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyEnv));
		} catch (IllegalArgumentException e) {
			// Fallback si no está en Base64 (útil en dev)
			this.secretKey = Keys.hmacShaKeyFor(secretKeyEnv.getBytes(StandardCharsets.UTF_8));
		}
	}

	public String generarToken(String username) {
		Date ahora = new Date();
		Date expiracion = new Date(ahora.getTime() + expirationMs);

		return Jwts.builder().subject(username).issuedAt(ahora).expiration(expiracion).signWith(secretKey).compact();
	}

	public Claims validarToken(String token) {
		try {
			return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
		} catch (JwtException e) {
			// Puede ser ExpiredJwtException, UnsupportedJwtException, etc.
			throw new IllegalArgumentException("Token JWT inválido o expirado", e);
		}
	}
}
