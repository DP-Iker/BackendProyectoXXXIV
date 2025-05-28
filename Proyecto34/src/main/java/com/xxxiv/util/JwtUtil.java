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

	/**
	 * Genera un token
	 * @param username Nombre de usuario
	 * @param esAdmin Si es administrador
	 * @return Devuelve un token
	 */
	public String generarToken(String username, boolean esAdmin) {
		Date ahora = new Date();
		Date expiracion = new Date(ahora.getTime() + expirationMs);

		return Jwts.builder()
				.subject(username)
				.issuedAt(ahora)
				.expiration(expiracion)
				.claim("esAdmin", esAdmin)
				.signWith(secretKey)
				.compact();
	}
	
	/**
	 * Genera un token de corto tiempo para cambiar la contraseña
	 * @param email Email del usuario
	 * @return Devuelve el token 
	 */
	public String generarTokenRecuperacion(String email) {
	    Date ahora = new Date();
	    Date expiracion = new Date(ahora.getTime() + 15 * 60 * 1000); // 15 min

	    return Jwts.builder()
	            .subject(email)
	            .issuedAt(ahora)
	            .expiration(expiracion)
	            .claim("tipo", "recuperacion")
	            .signWith(secretKey)
	            .compact();
	}

	/**
	 * Valida el token
	 * @param token Token
	 * @return Devuelve el Claims
	 */
	public Claims validarToken(String token) {
		try {
			return Jwts.parser()
					.verifyWith(secretKey)
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (JwtException e) {
			throw new IllegalArgumentException("Token JWT inválido o expirado", e);
		}
	}
	
	/**
	 * Valida el token de recuperacion
	 * @param token Token
	 * @return Devuelve el Claims
	 */
	public Claims validarTokenRecuperacion(String token) {
	    Claims claims = validarToken(token);
	    
	    // Si no es de tipo recuperación, da error
	    if (!"recuperacion".equals(claims.get("tipo", String.class))) {
	        throw new IllegalArgumentException("Token no válido para recuperación");
	    }
	    
	    return claims;
	}
}
