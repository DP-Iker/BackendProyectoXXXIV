package com.xxxiv.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.FiltroUsuariosDTO;
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
	
	/**
	 * Busca al usuario por el nombre de usuario
	 * 
	 * @param usuario Nombre de usuario
	 * @return Usuario
	 */
	public Optional<Usuario> buscarPorUsuario(String usuario) {
		return usuarioRepository.findByUsuario(usuario);
	}

	// DELETE
	/**
	 * Elimina al usuario de la BD
	 * 
	 * @param id ID del usuario
	 * @return Mensaje diciendo si se ha borrado
	 */
	public void eliminarUsuario(int id) {
		Usuario usuario = buscarPorId(id)
		        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + id + " no encontrado"));
		
		
		usuarioRepository.delete(usuario);
	}
	
	/**
	 * Hace admin al usuario indicado
	 * 
	 * @param id ID del usuario
	 */
	public void hacerAdmin(int id) {
		Usuario usuario = buscarPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + id + " no encontrado"));
		
		usuario.setEsAdministrador(true);
		usuarioRepository.save(usuario);;
	}
	
	/**
	 * Bloquea al usuario con el ID indicado
	 * 
	 * @param id ID del usuario
	 * @param mensaje Mensaje sobre el motivo del bloqueo
	 */
	public void bloquearUsuario(int id, String mensaje) {
		Usuario usuario = buscarPorId(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID " + id + " no encontrado"));
		
		usuario.setEstaBloqueado(true);
		usuario.setMotivoBloqueo(mensaje);
		usuarioRepository.save(usuario);
	}
	
	/**
	 * Cambia el email del usuario
	 * 
	 * @param nombreUsuario Nombre de usuario
	 * @param email Email
	 */
	public void cambiarEmail(String nombreUsuario, String email) {
		Usuario usuario = usuarioRepository.findByUsuario(nombreUsuario)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID no encontrado"));
		
		// Revisa si existe un usuario con ese correo
		if (usuarioRepository.existsByEmail(email)) {
			new ResponseStatusException(HttpStatus.CONFLICT, "El email ya está en uso");
		}
		
		usuario.setEmail(email);
		usuarioRepository.save(usuario);
	}
	
	/**
	 * Cambia la foto de perfil del usuario
	 * 
	 * @param nombreUsuario Nombre de usuario
	 * @param email Email
	 */
	public void cambiarFotoPerfil(String nombreUsuario, String foto) {
		Usuario usuario = usuarioRepository.findByUsuario(nombreUsuario)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario con ID no encontrado"));
		
		usuario.setFoto(foto);
		usuarioRepository.save(usuario);
	}
}
