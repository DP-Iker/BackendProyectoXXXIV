package com.xxxiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxxiv.model.Viaje;

import java.util.List;

public interface ViajeRepository extends JpaRepository<Viaje, Integer> {
    List<Viaje> findByFechaFinIsNull();
}