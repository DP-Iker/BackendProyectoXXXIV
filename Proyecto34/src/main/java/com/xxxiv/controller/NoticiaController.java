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
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/noticias")
@SecurityRequirement(name = "bearerAuth")
public class NoticiaController {

    @Autowired
    NoticiaService noticiaService;

    @Autowired
    UsuarioService usuarioService;

    /**
     * GET /api/noticias
     * Devuelve todas las noticias.
     */
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
        // 1) Controlar tamaño máximo por página
        int maxPageSize = 50;
        int size = pageable.getPageSize() > maxPageSize
                ? maxPageSize
                : pageable.getPageSize();
        Pageable safePageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());

        // 2) Construir el DTO de filtro
        FiltroNoticiasDTO filtro = new FiltroNoticiasDTO();
        filtro.setTitulo(titulo);
        filtro.setUsuarioId(usuarioId);
        filtro.setIdiomaCodigo(idiomaCodigo);
        filtro.setCreatedAt(createdAt);

        // 3) Invocar el servicio para obtener Page<Noticia>
        Page<Noticia> pageEntidades = noticiaService.buscarNoticias(filtro, safePageable);

        // 4) Mapear cada Noticia a NoticiaDTO
        Page<NoticiaDTO> pageDto = pageEntidades.map(NoticiaDTO::fromEntity);

        return ResponseEntity.ok(pageDto);
    }

    /**
     * GET /api/noticias/{id}
     * Recupera una noticia por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Noticia> obtenerPorId(@PathVariable Integer id) {
        try {
            Noticia noticia = noticiaService.obtenerPorId(id);
            return ResponseEntity.ok(noticia);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * POST /api/noticias
     * Crea una nueva noticia.
     * Se espera un JSON como:
     * {
     *   "titulo": "...",
     *   "contenido": "...",
     *   "idiomaCodigo": "ESP",
     *   "usuario": { "id": 42 }
     * }
     * O bien se puede enviar sólo { "titulo": "...", "contenido": "...", "idiomaCodigo": "ESP", "usuarioId": 42 }
     */
//    @PostMapping
//    public ResponseEntity<Noticia> crear(@RequestBody Noticia payload) {
//        try {
//            // Si en el JSON se envía sólo usuario.id (es decir, payload.getUsuario().getId()), lo tomamos:
//            String usuarioUsername = payload.getUsuario() != null ? payload.getUsuario().getUsuario() : null;
//            if (usuarioUsername == null) {
//                return ResponseEntity.badRequest().build();
//            }
//            // Creamos la entidad nueva:
//            Noticia creada = noticiaService.crearNoticia(payload, usuarioUsername);
//            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
//        } catch (EntityNotFoundException ex) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
    @PostMapping
    public ResponseEntity<Noticia> crear(@RequestBody CrearNoticiaDTO dto) {
        try {
            // Validar campos mínimos necesarios
            if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            // Buscar usuario por su username (o correo, según corresponda)
            Usuario usuario = usuarioService.buscarPorUsuario(dto.getUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Convertir el DTO a entidad Noticia
            Noticia noticia = new Noticia();
            noticia.setTitulo(dto.getTitulo());
            noticia.setContenido(dto.getContenido());
            noticia.setIdiomaCodigo(Idioma.valueOf(dto.getIdiomaCodigo()));
            noticia.setCreatedAt(dto.getFecha());
            noticia.setUsuario(usuario);

            // Guardar la noticia
            Noticia creada = noticiaService.crearNoticia(noticia, usuario.getUsuario());

            return ResponseEntity.status(HttpStatus.CREATED).body(creada);

        } catch (IllegalArgumentException e) {
            // Si el idioma es inválido, por ejemplo
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /api/noticias/{id}
     * Actualiza la noticia con id={id}.
     * JSON esperado similar a POST; se debe enviar ID del usuario en el que se editará (o null para dejar igual).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Noticia> actualizar(
            @PathVariable Integer id,
            @RequestBody CrearNoticiaDTO dto) {

        try {
            // Validar campos mínimos necesarios
            if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            // Buscar usuario por su username (o correo, según corresponda)
            Usuario usuario = usuarioService.buscarPorUsuario(dto.getUsuario())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

            // Convertir el DTO a entidad Noticia
            Noticia noticia = new Noticia();
            noticia.setTitulo(dto.getTitulo());
            noticia.setContenido(dto.getContenido());
            noticia.setIdiomaCodigo(Idioma.valueOf(dto.getIdiomaCodigo()));
            noticia.setCreatedAt(dto.getFecha());
            noticia.setUsuario(usuario);

            // Guardar la noticia
            Noticia update = noticiaService.actualizarNoticia(id, noticia, usuario.getUsuario());

            return ResponseEntity.ok(update);

        } catch (IllegalArgumentException e) {
            // Si el idioma es inválido, por ejemplo
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * DELETE /api/noticias/{id}
     * Elimina la noticia con id={id}.
     */
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
