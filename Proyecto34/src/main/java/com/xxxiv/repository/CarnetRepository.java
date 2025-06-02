package com.xxxiv.repository;

import com.xxxiv.model.Carnet;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.enums.EstadoCarnet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarnetRepository extends JpaRepository<Carnet, Integer> {
    Optional<Carnet> findByUsuario(Usuario usuario);

    Optional<Carnet> findByUsuarioUsuario(String usuario);
    List<Carnet> findByEstadoIn(Collection<EstadoCarnet> estados);

}