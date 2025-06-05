package com.xxxiv.repository;

import com.xxxiv.model.Carnet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarnetRepository extends JpaRepository<Carnet, Integer> {
	List<Carnet> findByEstaValidadoFalseOrderByFechaSolicitudAsc();
	boolean existsByUsuarioId(Integer usuarioId);
}