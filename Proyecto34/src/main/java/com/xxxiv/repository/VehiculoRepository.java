package com.xxxiv.repository;

import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {
    List<Vehiculo> findByEstado(Estado estado);
}
