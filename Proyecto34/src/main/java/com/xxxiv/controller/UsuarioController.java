package com.xxxiv.controller;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.BloqueoUsuarioDTO;
import com.xxxiv.dto.EmailDTO;
import com.xxxiv.dto.FiltroUsuariosDTO;
import com.xxxiv.dto.UsuarioDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.service.ImgurService;
import com.xxxiv.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	private final UsuarioService usuarioService;
	private final ImgurService imgurService;

	// GET
	@GetMapping
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Devuelve todos los usuarios", description = "Devuelve todos los usuarios que hay en la BD")
	@Parameters({ @Parameter(name = "page", description = "Número de página", example = "0"),
			@Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
			@Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc") })
	public ResponseEntity<Page<Usuario>> getUsuarios(@RequestParam(required = false) String usuario,
			@RequestParam(required = false) String email, @RequestParam(required = false) Boolean estaBloqueado,
			@RequestParam(required = false) Boolean esAdministrador,
			@RequestParam(required = false) LocalDateTime createdAt, Pageable pageable) {
		// Controla que el tamaño no sea excesivo
		int maxPageSize = 50; // Límite de elementos por página
		int size = pageable.getPageSize() > maxPageSize ? maxPageSize : pageable.getPageSize();

		Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

		// Crea el filtro
		FiltroUsuariosDTO filtro = new FiltroUsuariosDTO();
		filtro.setUsuario(usuario);
		filtro.setEmail(email);
		filtro.setEstaBloqueado(estaBloqueado);
		filtro.setEsAdministrador(esAdministrador);
		filtro.setCreatedAt(createdAt);

		Page<Usuario> usuarios = usuarioService.buscarUsuarios(filtro, safePageable);
		return ResponseEntity.ok(usuarios);
	}

	@GetMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Devuelve al usuario por ID", description = "Devuelve todos los datos del usuario de esa ID que hay en la BD")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
		return usuarioService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/me")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Devuelve al usuario según su token de sesión", description = "Devuelve todos los datos del usuario que pide la información")
	public ResponseEntity<UsuarioDTO> obtenerUsuarioAutenticado(Authentication authentication) {
		String username = authentication.getName();
		Usuario usuarioDb = usuarioService.buscarPorUsuario(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

		// Crea el DTO
		UsuarioDTO usuario = new UsuarioDTO();
		usuario.setUsername(usuarioDb.getUsuario());
		usuario.setEmail(usuarioDb.getEmail());
		usuario.setFotoUrl(usuarioDb.getFoto());
		return ResponseEntity.ok(usuario);
	}

	// PATCH
	@PatchMapping("/admin/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Hace admin al usuario indicado", description = "Da permisos de administrador al usuario seleccionado")
	public ResponseEntity<Boolean> hacerAdmin(@PathVariable int id) {
		usuarioService.hacerAdmin(id);

		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/bloquear/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Bloquea usuario indicado", description = "Bloquea al usuario seleccionado")
	public ResponseEntity<Boolean> bloquearUsuario(@PathVariable int id, @RequestBody @Valid BloqueoUsuarioDTO dto) {
		String mensaje = dto.getMensaje();
		usuarioService.bloquearUsuario(id, mensaje);

		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/me/cambiar-email")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Cambia el email", description = "Cambia el email del usuario logueado")
	public ResponseEntity<Void> cambiarEmail(Authentication authentication, @RequestBody @Valid EmailDTO dto) {
		String usuario = authentication.getName();
		String email = dto.getEmail();
		
		usuarioService.cambiarEmail(usuario, email);

		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping(value = "/me/cambiar-foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Cambia la foto de perfil", description = "Cambia la foto de perfil del usuario logueado")
	public ResponseEntity<?> cambiarFotoPerfil(Authentication authentication, @RequestPart("foto") MultipartFile foto) throws IOException {
		// No envia nada
		if (foto == null || foto.isEmpty()) {
	        return ResponseEntity.badRequest().body("No se ha subido ningún archivo");
	    }

		// El archivo no es una foto
	    if (!foto.getContentType().startsWith("image/")) {
	        return ResponseEntity.badRequest().body("Solo se permiten archivos de imagen");
	    }
		
		String usuario = authentication.getName();
		String urlFoto = imgurService.subirImagen(foto.getBytes());
		
		usuarioService.cambiarFotoPerfil(usuario, urlFoto);

		return ResponseEntity.noContent().build();
	}

	// DELETE
	@DeleteMapping("/{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Elimina al usuario por ID", description = "Elimina al usuario de la BD con el ID")
	public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {
		usuarioService.eliminarUsuario(id);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/me")
	@SecurityRequirement(name = "bearerAuth")
	@Operation(summary = "Elimina al usuario según su token de sesión", description = "Elimina al usuario de la BD con el token de sesión")
	public ResponseEntity<Void> eliminarUsuarioPropio(Authentication authentication) {
		String username = authentication.getName();
		Usuario usuario = usuarioService.buscarPorUsuario(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

		usuarioService.eliminarUsuario(usuario.getId());

		return ResponseEntity.noContent().build();
	}
}
