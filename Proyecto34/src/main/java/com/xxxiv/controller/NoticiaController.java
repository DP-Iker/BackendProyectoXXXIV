package com.xxxiv.controller;

import com.xxxiv.dto.CrearNoticiaDTO;
import com.xxxiv.dto.FiltroNoticiasDTO;
import com.xxxiv.dto.NoticiaDTO;
import com.xxxiv.model.Noticia;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.enums.Idioma;
import com.xxxiv.service.ImgurService;
import com.xxxiv.service.NoticiaService;
import com.xxxiv.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noticias")
public class NoticiaController {

	private final NoticiaService noticiaService;
	private final UsuarioService usuarioService;
	private final ImgurService imgurService;

	@GetMapping
	@Operation(summary = "Devuelve todas las noticias", description = "Permite filtrar, paginar y ordenar las noticias existentes en la BD")
	@Parameters({ @Parameter(name = "titulo", description = "Filtro por título (contains)", example = "versión"),
			@Parameter(name = "usuarioId", description = "Filtro por ID del usuario que creó la noticia", example = "42"),
			@Parameter(name = "idiomaCodigo", description = "Filtro por código de idioma (CAT, ESP, EN)", example = "ESP"),
			@Parameter(name = "createdAt", description = "Filtro por fecha de creación (yyyy-MM-dd'T'HH:mm:ss)", example = "2025-06-01T00:00:00"),
			@Parameter(name = "page", description = "Número de página (paginación)", example = "0"),
			@Parameter(name = "size", description = "Cantidad de elementos por página (paginación)", example = "10"),
			@Parameter(name = "sort", description = "Ordenamiento (campo,dirección). Ej: id,asc o titulo,desc", example = "id,asc") })
	public ResponseEntity<Page<NoticiaDTO>> getNoticias(@RequestParam(required = false) String titulo,
			@RequestParam(required = false) Integer usuarioId, @RequestParam(required = false) String idiomaCodigo,
			@RequestParam(required = false) LocalDateTime createdAt, Pageable pageable) {
		int maxPageSize = 50;
		int size = pageable.getPageSize() > maxPageSize ? maxPageSize : pageable.getPageSize();
		Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

		FiltroNoticiasDTO filtro = new FiltroNoticiasDTO();
		filtro.setTitulo(titulo);
		filtro.setUsuarioId(usuarioId);
		filtro.setIdiomaCodigo(idiomaCodigo);
		filtro.setCreatedAt(createdAt);

		Page<Noticia> pageEntidades = noticiaService.buscarNoticias(filtro, safePageable);
		Page<NoticiaDTO> pageDto = pageEntidades.map(NoticiaDTO::fromEntity);

		return ResponseEntity.ok(pageDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Noticia> obtenerPorId(@PathVariable Integer id) {
		try {
			Noticia noticia = noticiaService.obtenerPorId(id);
			return ResponseEntity.ok(noticia);
		} catch (EntityNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Noticia> crear(
	        @RequestPart("dto") CrearNoticiaDTO dto,
	        @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

	    try {
	        if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
	            return ResponseEntity.badRequest().build();
	        }

	        String imagenUrl = null;
	        if (imagen != null && !imagen.isEmpty()) {
	            imagenUrl = imgurService.subirImagen(imagen.getBytes());
	        }

	        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(dto.getUsuario());

	        Noticia noticia = new Noticia();
	        noticia.setTitulo(dto.getTitulo());
	        noticia.setContenido(dto.getContenido());
	        noticia.setImagenUrl(imagenUrl);
	        noticia.setIdiomaCodigo(Idioma.valueOf(dto.getIdiomaCodigo()));
	        noticia.setCreatedAt(dto.getFecha());
	        noticia.setUsuario(usuario);

	        Noticia creada = noticiaService.crearNoticia(noticia, usuario.getUsuario());

	        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().build();
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	    }
	}


	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Noticia> actualizar(@PathVariable Integer id, @RequestBody CrearNoticiaDTO dto) {
		try {

			if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
				return ResponseEntity.badRequest().build();
			}

			Usuario usuario = usuarioService.obtenerUsuarioPorNombre(dto.getUsuario());

			Noticia noticia = new Noticia();
			noticia.setTitulo(dto.getTitulo());
			noticia.setContenido(dto.getContenido());
			noticia.setIdiomaCodigo(Idioma.valueOf(dto.getIdiomaCodigo()));
			noticia.setCreatedAt(dto.getFecha());
			noticia.setUsuario(usuario);

			Noticia update = noticiaService.actualizarNoticia(id, noticia, usuario.getUsuario());

			return ResponseEntity.ok(update);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
		try {
			noticiaService.eliminarNoticia(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
