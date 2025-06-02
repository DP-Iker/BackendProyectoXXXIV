package com.xxxiv.controller;

import com.xxxiv.dto.CrearNoticiaDTO;
import com.xxxiv.dto.FiltroNoticiasDTO;
import com.xxxiv.dto.NoticiaDTO;
import com.xxxiv.model.Noticia;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.enums.Idioma;
import com.xxxiv.service.NoticiaService;
import com.xxxiv.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/noticias")
@SecurityRequirement(name = "bearerAuth")
public class NoticiaController {

    @Autowired
    NoticiaService noticiaService;

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    @Operation(
            summary = "Devuelve todas las noticias",
            description = "Permite filtrar, paginar y ordenar las noticias existentes en la BD"
    )
    @Parameters({
            @Parameter(name = "titulo",        description = "Filtro por título (contains)",                     example = "versión"),
            @Parameter(name = "usuarioId",     description = "Filtro por ID del usuario que creó la noticia",      example = "42"),
            @Parameter(name = "idiomaCodigo",  description = "Filtro por código de idioma (CAT, ESP, EN)",           example = "ESP"),
            @Parameter(name = "createdAt",     description = "Filtro por fecha de creación (yyyy-MM-dd'T'HH:mm:ss)", example = "2025-06-01T00:00:00"),
            @Parameter(name = "page",          description = "Número de página (paginación)",                        example = "0"),
            @Parameter(name = "size",          description = "Cantidad de elementos por página (paginación)",        example = "10"),
            @Parameter(name = "sort",          description = "Ordenamiento (campo,dirección). Ej: id,asc o titulo,desc", example = "id,asc")
    })
    public ResponseEntity<Page<NoticiaDTO>> getNoticias(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer usuarioId,
            @RequestParam(required = false) String idiomaCodigo,
            @RequestParam(required = false) LocalDateTime createdAt,
            Pageable pageable
    ) {
        int maxPageSize = 50;
        int size = pageable.getPageSize() > maxPageSize
                ? maxPageSize
                : pageable.getPageSize();
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

    @PostMapping
    public ResponseEntity<Noticia> crear(@RequestBody CrearNoticiaDTO dto) {
        try {
            if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            Usuario usuario = usuarioService.buscarPorUsuario(dto.getUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));


            Noticia noticia = new Noticia();
            noticia.setTitulo(dto.getTitulo());
            noticia.setContenido(dto.getContenido());
            noticia.setIdiomaCodigo(Idioma.valueOf(dto.getIdiomaCodigo()));
            noticia.setCreatedAt(dto.getFecha());
            noticia.setUsuario(usuario);

            Noticia creada = noticiaService.crearNoticia(noticia, usuario.getUsuario());

            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException e) {
            // Si el idioma es inválido, por ejemplo
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Noticia> actualizar(
            @PathVariable Integer id,
            @RequestBody CrearNoticiaDTO dto) {
        try {

            if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            Usuario usuario = usuarioService.buscarPorUsuario(dto.getUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

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
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        try {
            noticiaService.eliminarNoticia(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
