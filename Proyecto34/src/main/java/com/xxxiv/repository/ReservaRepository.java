package com.xxxiv.repository;

import com.xxxiv.model.Reserva;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.enums.EstadoReserva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>, JpaSpecificationExecutor<Reserva> {
	List<Reserva> findByEstadoAndFechaReservaBefore(EstadoReserva estado, LocalDateTime cutoff);
	
	boolean existsByUsuarioAndEstado(Usuario usuario, EstadoReserva estado);
}
