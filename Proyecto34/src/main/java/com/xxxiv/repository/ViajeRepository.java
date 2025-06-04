package com.xxxiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xxxiv.model.Viaje;

import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Integer>, JpaSpecificationExecutor<Viaje> {
    List<Viaje> findByFechaFinIsNull();

    List<Viaje> findByUsuarioUsuarioAndFechaFinIsNotNullOrderByFechaInicioDesc(String nombreUsuario);
}