package com.xxxiv.repository;

import com.xxxiv.model.Carnet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarnetRepository extends JpaRepository<Carnet, Integer> {
    // Puedes agregar consultas personalizadas si necesitas
}
