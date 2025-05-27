package com.xxxiv.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.xxxiv.model.Usuario;
import com.xxxiv.repository.UsuarioRepository;
import com.xxxiv.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
	 * Comprueba que el usuario y contraseña indicados coinciden con la BD
	 * 
	 * @param usuario     Nombre de usuario
	 * @param contrasenya Contraseña enviada
	 * @return Devuelve el token
	 */
    public String login(String usuario, String contrasenya) {
    	Usuario usuarioDB = usuarioRepository.findByUsuario(usuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    	// Si no encuentra la contraseña
        if (!passwordEncoder.matches(contrasenya, usuarioDB.getContrasenya())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generarToken(usuarioDB.getUsuario());
    }
}

