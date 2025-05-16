package com.xxxiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxxiv.model.Caracteristica;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Integer> {
    // Aquí puedes añadir consultas personalizadas si las necesitas.
}