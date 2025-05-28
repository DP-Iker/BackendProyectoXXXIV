package com.xxxiv.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.FiltroUsuariosDTO;
import com.xxxiv.dto.UsuarioDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {
	private final UsuarioService usuarioService;

	// GET
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Devuelve todos los usuarios", description = "Devuelve todos los usuarios que hay en la BD")
	@Parameters({
	    @Parameter(name = "page", description = "Número de página", example = "0"),
	    @Parameter(name = "size", description = "Cantidad de elementos por página", example = "10"),
	    @Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o usuario,desc", example = "id,asc")
	})
	public ResponseEntity<Page<Usuario>> getUsuarios(
			@RequestParam(required = false) String usuario,
			@RequestParam(required = false) String email, 
			@RequestParam(required = false) Boolean estaBloqueado,
			@RequestParam(required = false) Boolean esAdministrador,
			@RequestParam(required = false) LocalDateTime createdAt,
			Pageable pageable
	) {
		// Controla que el tamaño no sea excesivo
		int maxPageSize = 50;  // Límite de elementos por página
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
	@Operation(summary = "Devuelve al usuario por ID", description = "Devuelve todos los datos del usuario de esa ID que hay en la BD")
	public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
		return usuarioService.buscarPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@GetMapping("/me")
	@Operation(summary = "Devuelve al usuario según su token de sesión", description = "Devuelve todos los datos del usuario que pide la información")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioAutenticado(Authentication authentication) {
        String username = authentication.getName();
        Usuario usuarioDb = usuarioService.buscarPorUsuario(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        // Crea el DTO
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setUsername(usuarioDb.getUsuario());
        usuario.setEmail(usuarioDb.getEmail());
        return ResponseEntity.ok(usuario);
    }

	// DELETE
	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina al usuario por ID", description = "Elimina al usuario de la BD con el ID")
	public boolean eliminarUsuario(@PathVariable int id) {
		return usuarioService.eliminarUsuario(id);
	}
}
