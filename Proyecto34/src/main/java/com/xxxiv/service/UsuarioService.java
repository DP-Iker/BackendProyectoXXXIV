package com.xxxiv.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;

	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
	
	public Optional<Usuario> findById(int id) {
        return usuarioRepository.findById(id);
    }

	public Usuario crearUsuario(String usuario, String contrasenya, String email) {
		// Verifica que el usuario es único
		if (usuarioRepository.existsByUsuario(usuario)) {
			throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
		}

		// Verifica que el email es único
		if (usuarioRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("El email ya está en uso.");
		}

		// Crea el usuario
		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setUsuario(usuario);
		nuevoUsuario.setContrasenya(contrasenya);
		nuevoUsuario.setEmail(email);
		nuevoUsuario.setEstaBloqueado(false);
		nuevoUsuario.setCreatedAt(java.time.LocalDateTime.now());

		return usuarioRepository.save(nuevoUsuario);
	}
}
