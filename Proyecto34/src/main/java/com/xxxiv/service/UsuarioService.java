package com.xxxiv.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.xxxiv.dto.FiltroUsuariosDTO;
import com.xxxiv.exception.EmailException;
import com.xxxiv.model.Usuario;
import com.xxxiv.repository.UsuarioRepository;
import com.xxxiv.specifications.UsuarioSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;

	// GET
	/**
	 * Busca los usuarios según el filtro y los devuelve en página
	 * 
	 * @param filtro   Campos por los que se puede filtrar
	 * @param pageable Página
	 * @return Página con los elementos
	 */
	public Page<Usuario> buscarUsuarios(FiltroUsuariosDTO filtro, Pageable pageable) {
		Specification<Usuario> filtrosAplicados = UsuarioSpecification.buildSpecification(filtro);
		return usuarioRepository.findAll(filtrosAplicados, pageable);
	}

	/**
	 * Busca al usuario con el ID indicado
	 * 
	 * @param id ID del usuario
	 * @return Usuario con el ID
	 */
	public Optional<Usuario> buscarPorId(int id) {
		return usuarioRepository.findById(id);
	}

	// POST
	/**
	 * Crea un usuario en la BD
	 * 
	 * @param usuario     Nombre de usuario
	 * @param contrasenya Contraseña
	 * @param email       Correo electrónico
	 * @return Usuario creado
	 */
	public Usuario crearUsuario(String usuario, String contrasenya, String email) {
		// Verifica que el usuario es único
		if (usuarioRepository.existsByUsuario(usuario)) {
			throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
		}

		// Verifica que el email es único
		if (usuarioRepository.existsByEmail(email)) {
			throw new IllegalArgumentException("El email ya está en uso.");
		}

		// Hashea la contraseña
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String contrasenyaHasheada = passwordEncoder.encode(contrasenya);

		// Crea el usuario
		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setUsuario(usuario);
		nuevoUsuario.setContrasenya(contrasenyaHasheada);
		nuevoUsuario.setEmail(email);
		nuevoUsuario.setEstaBloqueado(false);
		nuevoUsuario.setCreatedAt(java.time.LocalDateTime.now());

		return usuarioRepository.save(nuevoUsuario);
	}

	/**
	 * Comprueba que el usuario y contraseña indicados coinciden con la BD
	 * 
	 * @param usuario     Nombre de usuario
	 * @param contrasenya Contraseña enviada
	 * @return Booleano sobre si coinciden las credenciales
	 */
	public boolean loginUsuario(String usuario, String contrasenya) {
		// Verifica que el usuario existe
		Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuario);

		if (usuarioOpt.isEmpty()) {
			return false; // Si no existe, devuelve false
		}
		Usuario usuarioDB = usuarioOpt.get();

		// Usa BCrypt para comparar
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(contrasenya, usuarioDB.getContrasenya());
	}

//	public void enviarCodigo(String email) throws EmailException {
//		try {
//	        
//			
//	    } catch (MessagingException e) {
//	        throw new EmailException("Error enviando correo", e);
//	    }
//	}

	// PATCH
//	public boolean cambiarContrasenya(int id,) {
//		Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
//		
//		if (usuarioOpt.isEmpty()) {
//			return false;
//		}
//		Usuario usuario = usuarioOpt.get();
//		
//		
//		
//		return true;
//	}

	// DELETE
	/**
	 * Elimina al usuario de la BD
	 * 
	 * @param id ID del usuario
	 * @return Mensaje diciendo si se ha borrado
	 */
	public boolean eliminarUsuario(int id) {
		Optional<Usuario> usuarioAEliminar = buscarPorId(id);

		// Si no encuentra al usuario
		if (usuarioAEliminar.isEmpty()) {
			return false;
		}
		// Elimina al usuario
		usuarioRepository.deleteById(id);
		return true;
	}
}
