package com.xxxiv.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Crea un token de inicio de sedion para verificar el usuario
     * 
     * @param usuario Nombre de usuario
     * @param contrasenya Contraseña
     * @return Devuelve un String con el token del usuario
     */
    public String login(String usuario, String contrasenya) {
    	Usuario usuarioDB = usuarioRepository.findByUsuario(usuario)
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

    	// Si no encuentra la contraseña
        if (!passwordEncoder.matches(contrasenya, usuarioDB.getContrasenya())) {
        	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return jwtUtil.generarToken(usuarioDB.getUsuario(), usuarioDB.isEsAdministrador());
    }

    /**
     * Crea un usuario y lo añade a la BD
     * 
     * @param usuario Nombre de usuario
     * @param contrasenya Contraseña
     * @param email Email
     * @return Devuelve el usuario
     */
	public Usuario crearUsuario(String usuario, String contrasenya, String email) {
		if (usuarioRepository.existsByUsuario(usuario)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya está en uso.");
		}

		if (usuarioRepository.existsByEmail(email)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso.");
		}

		String contrasenyaHasheada = passwordEncoder.encode(contrasenya);

		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setUsuario(usuario);
		nuevoUsuario.setContrasenya(contrasenyaHasheada);
		nuevoUsuario.setEmail(email);
		nuevoUsuario.setEstaBloqueado(false);
		nuevoUsuario.setCreatedAt(java.time.LocalDateTime.now());

		return usuarioRepository.save(nuevoUsuario);
	}

	/**
	 * Envia un correo con el link para el cambio de contraseña
	 * 
	 * @param email Email al que enviar el correo
	 */
	public void enviarCorreoCambioContrasenya(String email) {
        Optional<Usuario> optional = usuarioRepository.findByEmail(email);

        if (optional.isPresent()) {
            String token = jwtUtil.generarTokenRecuperacion(optional.get().getEmail());
            String link = "http://localhost:5173/panel/pass?token=" + token;
            
            String cuerpoHTML = """
                    <p>Hola,</p>
                    <p>Has solicitado restablecer tu contraseña.</p>
                    <p>Haz click en el siguiente enlace para continuar:</p>
                    <a href="%s">%s</a>
                    <p>Si no solicitaste este cambio, ignora este correo.</p>
                    """.formatted(link, link);

                emailService.enviar(email, "Recupera tu contraseña", cuerpoHTML);
        }
    }

//	public void enviarCorreoPrueba() {
//	    String emailDestino = "";
//	    String asunto = "Correo de prueba";
//	    String cuerpo = "<p>Este es un correo de prueba enviado desde Spring Boot</p>";
//
//	    emailService.enviar(emailDestino, asunto, cuerpo);
//	    System.out.println("Se ha enviado el correo");
//	}

	/**
	 * Cambia la contraseña del usuario si tiene el token de cambio de contraseña
	 * 
	 * @param token Token de cambio de contraseña
	 * @param contrasenyaNueva Contraseña nueva
	 */
	public void cambiarContrasenya(String token, String contrasenyaNueva) {
		Claims claims = jwtUtil.validarTokenRecuperacion(token);
		String email = claims.getSubject();
		
		// Busca al usuario con el correo que dicta el token
		Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
		
		// Guarda la nueva contraseña en el usuario
        String contrasenya = passwordEncoder.encode(contrasenyaNueva);
        usuario.setContrasenya(contrasenya);

        usuarioRepository.save(usuario);
    }
}

