package com.xxxiv.specifications;

import com.xxxiv.dto.FiltroUsuariosDTO;
import com.xxxiv.model.Usuario;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioSpecification {

    public static Specification<Usuario> tieneUsuario(String usuario) {
        return (root, query, cb) ->
            usuario == null ? null : cb.like(cb.lower(root.get("usuario")), "%" + usuario.toLowerCase() + "%");
    }

    public static Specification<Usuario> tieneEmail(String email) {
        return (root, query, cb) ->
            email == null ? null : cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Usuario> estaBloqueado(Boolean estaBloqueado) {
        return (root, query, cb) ->
            estaBloqueado == null ? null : cb.equal(root.get("estaBloqueado"), estaBloqueado);
    }

    public static Specification<Usuario> esAdministrador(Boolean esAdministrador) {
        return (root, query, cb) ->
            esAdministrador == null ? null : cb.equal(root.get("esAdministrador"), esAdministrador);
    }

    public static Specification<Usuario> createdAt(LocalDateTime createdAt) {
        return (root, query, cb) ->
            createdAt == null ? null : cb.equal(root.get("createdAt"), createdAt);
    }

    public static Specification<Usuario> buildSpecification(FiltroUsuariosDTO filter) {
        return Specification.where(
                Optional.ofNullable(filter.getUsuario())
                        .map(UsuarioSpecification::tieneUsuario)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getEmail())
                        .map(UsuarioSpecification::tieneEmail)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getEstaBloqueado())
                        .map(UsuarioSpecification::estaBloqueado)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getEsAdministrador())
                        .map(UsuarioSpecification::esAdministrador)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getCreatedAt())
                        .map(UsuarioSpecification::createdAt)
                        .orElse(null));
    }
}
