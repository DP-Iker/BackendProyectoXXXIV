package com.xxxiv.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.xxxiv.model.Usuario;
import com.xxxiv.repository.UsuarioRepository;
import com.xxxiv.util.JwtUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmailService emailService;

    public String login(String usuario, String contrasenya) {
    	Usuario usuarioDB = usuarioRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    	// Si no encuentra la contraseña
        if (!passwordEncoder.matches(contrasenya, usuarioDB.getContrasenya())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuarioDB.getUsuario(), usuarioDB.isEsAdministrador());
    }

	public Usuario crearUsuario(String usuario, String contrasenya, String email) {
		if (usuarioRepository.existsByUsuario(usuario)) {
			throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
		}

		if (usuarioRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("El email ya está en uso.");
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String contrasenyaHasheada = passwordEncoder.encode(contrasenya);

		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setUsuario(usuario);
		nuevoUsuario.setContrasenya(contrasenyaHasheada);
		nuevoUsuario.setEmail(email);
		nuevoUsuario.setEstaBloqueado(false);
		nuevoUsuario.setCreatedAt(java.time.LocalDateTime.now());

		return usuarioRepository.save(nuevoUsuario);
	}

	public void enviarCorreoCambioContrasenya(String email) {
        Optional<Usuario> optional = usuarioRepository.findByEmail(email);

        if (optional.isPresent()) {
            String token = jwtUtil.generarTokenRecuperacion(optional.get().getEmail());
            String link = "https://localhost:5173/pass?token=" + token;
            
            String cuerpoHTML = """
                    <p>Hola,</p>
                    <p>Has solicitado restablecer tu contraseña.</p>
                    <p>Haz clic en el siguiente enlace para continuar:</p>
                    <a href="%s">%s</a>
                    <p>Si no solicitaste este cambio, ignora este correo.</p>
                    """.formatted(link, link);

                emailService.enviar(email, "Recupera tu contraseña", cuerpoHTML);
        }
    }

	public void enviarCorreoPrueba() {
	    String emailDestino = "";
	    String asunto = "Correo de prueba";
	    String cuerpo = "<p>Este es un correo de prueba enviado desde Spring Boot</p>";

	    emailService.enviar(emailDestino, asunto, cuerpo);
	    System.out.println("Se ha enviado el correo");
	}

	public void cambiarContrasenya(String token, String contrasenyaNueva) {
		Claims claims = jwtUtil.validarTokenRecuperacion(token);
		
		// Busca al usuario con el correo que dicta el token
		Usuario usuario = usuarioRepository.findByEmail(claims.getSubject())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
		
		// Guarda la nueva contraseña en el usuario
        String contrasenya = passwordEncoder.encode(contrasenyaNueva);
        usuario.setContrasenya(contrasenya);

        usuarioRepository.save(usuario);
    }

}

