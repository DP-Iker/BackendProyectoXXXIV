package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class NoticiaDTO {

    private Integer id;
    private String titulo;
    private String descripcion;
    private String contenido;
    private String imagen;
    private String fecha;      // ISO o el formato que quieras exponer
    private String autor;
    private Boolean publicado;
    private String idiomaCodigo;

    public static NoticiaDTO fromEntity(com.xxxiv.model.Noticia entidad) {
        NoticiaDTO dto = new NoticiaDTO();

        dto.setId(entidad.getId());
        dto.setTitulo(entidad.getTitulo());

        // Descripción: primeros 100 caracteres del contenido
        String contenidoCompleto = entidad.getContenido();
        if (contenidoCompleto.length() <= 100) {
            dto.setDescripcion(contenidoCompleto);
        } else {
            dto.setDescripcion(contenidoCompleto.substring(0, 100) + "...");
        }

        // Contenido completo
        dto.setContenido(contenidoCompleto);

        // Imagen: asumiendo que expones un endpoint GET /v1/noticias/{id}/imagen
        dto.setImagen("/v1/noticias/" + entidad.getId() + "/imagen");

        // Fecha: formateamos createdAt a ISO_LOCAL_DATE_TIME
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        dto.setFecha(entidad.getCreatedAt().format(formatter));

        // Autor: el nombre del usuario (asumiendo que `entidad.getUsuario().getNombre()` existe)
        dto.setAutor(entidad.getUsuario().getUsuario());

        dto.setIdiomaCodigo(entidad.getIdiomaCodigo().name());

        // Publicado: true porque existe createdAt. Si tu lógica es distinta, modifícala aquí.
        dto.setPublicado(entidad.getCreatedAt() != null);

        return dto;
    }
}
