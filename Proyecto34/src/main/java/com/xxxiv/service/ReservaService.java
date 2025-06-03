package com.xxxiv.service;

import com.xxxiv.dto.FiltroReservasDTO;
import com.xxxiv.dto.ReservaDetalleDTO;
import com.xxxiv.model.Parking;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ReservaRepository;
import com.xxxiv.specifications.ReservaSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public Page<Reserva> buscarReservas(FiltroReservasDTO filtro, Pageable pageable) {
		Specification<Reserva> filtrosAplicados = ReservaSpecification.buildSpecification(filtro);
		return reservaRepository.findAll(filtrosAplicados, pageable);
	}
}
