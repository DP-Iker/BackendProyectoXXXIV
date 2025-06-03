package com.xxxiv.repository;

import com.xxxiv.model.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>, JpaSpecificationExecutor<Reserva> {
    List<Reserva> findByUsuarioUsuarioOrderByFechaReservaDesc(String nombreUsuario);
}
