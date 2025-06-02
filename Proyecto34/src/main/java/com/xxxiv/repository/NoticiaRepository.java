package com.xxxiv.repository;

import com.xxxiv.model.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoticiaRepository  extends JpaRepository<Noticia, Integer> , JpaSpecificationExecutor<Noticia> {
    Optional<List<Noticia>> findByUsuarioId(Integer usuarioId);
}
