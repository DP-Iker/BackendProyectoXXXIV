package com.xxxiv.specifications;

import com.xxxiv.dto.FiltroNoticiasDTO;
import com.xxxiv.model.Noticia;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Optional;

public class NoticiaSpecification {

    public static Specification<Noticia> tituloContains(String titulo) {
        return (root, query, cb) ->
                titulo == null
                        ? null
                        : cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
    }

    public static Specification<Noticia> usuarioIdEquals(Integer usuarioId) {
        return (root, query, cb) ->
                usuarioId == null
                        ? null
                        : cb.equal(root.get("usuario").get("id"), usuarioId);
    }

    public static Specification<Noticia> idiomaEquals(String idiomaCodigo) {
        return (root, query, cb) ->
                idiomaCodigo == null
                        ? null
                        : cb.equal(root.get("idiomaCodigo"), idiomaCodigo);
    }

    public static Specification<Noticia> createdAtEquals(LocalDateTime createdAt) {
        return (root, query, cb) ->
                createdAt == null
                        ? null
                        : cb.equal(root.get("createdAt"), createdAt);
    }

    public static Specification<Noticia> buildSpecification(FiltroNoticiasDTO filter) {
        return Specification.where(
                        Optional.ofNullable(filter.getTitulo())
                                .map(NoticiaSpecification::tituloContains)
                                .orElse(null))
                .and(Optional.ofNullable(filter.getUsuarioId())
                        .map(NoticiaSpecification::usuarioIdEquals)
                        .orElse(null))
                .and(Optional.ofNullable(filter.getIdiomaCodigo())
                        .map(NoticiaSpecification::idiomaEquals)
                        .orElse(null))
                .and(Optional.ofNullable(filter.getCreatedAt())
                        .map(NoticiaSpecification::createdAtEquals)
                        .orElse(null));
    }
}
