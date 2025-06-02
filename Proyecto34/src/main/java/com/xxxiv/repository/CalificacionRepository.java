package com.xxxiv.repository;

import com.xxxiv.model.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    List<Calificacion> findByVehiculoId(Integer vehiculoId);
}
