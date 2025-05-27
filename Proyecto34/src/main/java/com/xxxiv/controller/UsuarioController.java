package com.xxxiv.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.dto.CrearUsuarioDTO;
import com.xxxiv.dto.EmailDTO;
import com.xxxiv.dto.FiltroUsuariosDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	private final UsuarioService usuarioService;

	// GET
	@GetMapping
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
	
//	@GetMapping("/me")
//    public ResponseEntity<Usuario> getUsuarioPropio() {
//        
//
//        return ResponseEntity.ok(currentUser);
//    }

	// POST
	@PostMapping
	@Operation(summary = "Crea un usuario", description = "Crea un usuario si le envias un nombre de usuario único, una contraseña y un email único")
	public ResponseEntity<Usuario> crearUsuario(@RequestBody @Valid CrearUsuarioDTO dto) {
		Usuario usuario = usuarioService.crearUsuario(dto.getUsuario(), dto.getContrasenya(), dto.getEmail());
		return new ResponseEntity<>(usuario, HttpStatus.CREATED);
	}
	
//	@PostMapping("/enviar-codigo")
//	@Operation(summary = "Inicia sesión del usuario", description = "Inicia sesión con el usuario y contraseña")
//	public void enviarCodigo(@RequestBody @Valid EmailDTO dto) {
//		return usuarioService.enviarCodigo(dto.getEmail());
//	}

	// DELETE
	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina al usuario por ID", description = "Elimina al usuario de la BD con el ID")
	public boolean eliminarUsuario(@PathVariable int id) {
		return usuarioService.eliminarUsuario(id);
	}
}
