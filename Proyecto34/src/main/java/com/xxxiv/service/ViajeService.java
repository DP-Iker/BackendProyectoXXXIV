package com.xxxiv.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.ViajeActualizarDTO;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ViajeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViajeService {

	private final VehiculoService vehiculoService;
	private final ViajeRepository viajeRepository;
	
	
	public List<Viaje> findAll() {
		return viajeRepository.findAll();
	}

	/**
	 * Busca un viaje por ID
	 *
	 * @param id ID del viaje
	 * @return Develve un Viaje
	 */
	public Viaje obtenerViajePorId(Integer id) {
		return viajeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Viaje con id "+ id +" no encontrado"));
	}
	
	/**
	 * Crea un viaje a partir de la reserva
	 * 
	 * @param reserva 
	 * @return
	 */
	public Viaje crearViaje(Reserva reserva) {
		Viaje viaje = new Viaje();
	    viaje.setUsuario(reserva.getUsuario());
	    viaje.setVehiculo(reserva.getVehiculo());
	    viaje.setFechaInicio(LocalDate.now());
	    viaje.setCods(new ArrayList<>());

	    return viajeRepository.save(viaje);
	}
	
	public Viaje actualizarParcialmente(Integer id, ViajeActualizarDTO dto) {
	    Viaje viaje = obtenerViajePorId(id);

	    if (dto.getLatitud() != null && dto.getLongitud() != null) {
	        vehiculoService.actualizarUbicacion(viaje.getVehiculo().getId(), dto.getLatitud(), dto.getLongitud());
	    }

	    if (dto.getFechaFin() != null) {
	        viaje.setFechaFin(dto.getFechaFin());
	    }

	    if (dto.getKmRecorridos() != null) {
	        viaje.setKmRecorridos(dto.getKmRecorridos());
	    }

	    return viajeRepository.save(viaje);
	}

	public Viaje finalizarViaje(Integer viajeId, LocalDate fechaFin, Integer kmRecorridos) {
		Viaje viaje = viajeRepository.findById(viajeId).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

		viaje.finalizarViaje(fechaFin, kmRecorridos);

		return viajeRepository.save(viaje);
	}

	public void deleteById(Integer id) {
		viajeRepository.deleteById(id);
	}
}
