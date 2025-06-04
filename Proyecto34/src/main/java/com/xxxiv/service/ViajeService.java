package com.xxxiv.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.FiltroViajesDTO;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Tipo;
import com.xxxiv.repository.ViajeRepository;
import com.xxxiv.specifications.ViajeSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ViajeService {

	private final VehiculoService vehiculoService;
	private final ViajeRepository viajeRepository;
	
	public Page<Viaje> buscarViajes(FiltroViajesDTO filtro, Pageable pageable) {
		Specification<Viaje> filtrosAplicados = ViajeSpecification.buildSpecification(filtro);
		return viajeRepository.findAll(filtrosAplicados, pageable);
	}

	/**
	 * Busca un viaje por ID
	 *
	 * @param id ID del viaje
	 * @return Develve un Viaje
	 */
	public Viaje obtenerViajePorId(int id) {
		return viajeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Viaje con id "+ id +" no encontrado"));
	}
	
	public Viaje obtenerViajePropioPorId(String nombreUsuario, int id) {
		Viaje viaje = obtenerViajePorId(id);
		
		// Si el viaje no es del usuario del token, no lo devuelve
		if (!viaje.getUsuario().getUsuario().equals(nombreUsuario)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "El viaje no te pertenece");
		}
		return viaje;
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
	    viaje.setFechaInicio(LocalDateTime.now());
	    viaje.setCods(new ArrayList<>());

	    return viajeRepository.save(viaje);
	}
	
	@Transactional
	public Viaje finalizarViaje(int id, String nombreUsuario, int kilometrajeNuevo) {
		Viaje viaje = obtenerViajePropioPorId(nombreUsuario, id);
		Vehiculo vehiculo = viaje.getVehiculo();
		
		// Resta los km que tenia antes del viaje a los que tiene ahora para obtener los recorridos
		int kmRecorridos = kilometrajeNuevo - viaje.getVehiculo().getKilometraje();
		double precio = calcularPrecioViaje(vehiculo.getTipo(), kmRecorridos);
		
		// Pone el vehículo en disponible
		vehiculo.setKilometraje(kilometrajeNuevo);
		vehiculo.setEstado(Estado.DISPONIBLE);
		vehiculoService.guardarVehiculo(vehiculo);
		
		// Actualiza los datos del viaje
		viaje.setFechaFin(LocalDateTime.now());
		viaje.setKmRecorridos(kmRecorridos);
		viaje.setPrecio(precio);

		return viajeRepository.save(viaje);
	}

	public void eliminarViaje(Integer id) {
		viajeRepository.deleteById(id);
	}
	
	/**
	 * Formula con el cálculo del precio
	 * 
	 * @param tipoVehiculo Tipo del vehículo
	 * @param kmRecorridos km recorridos
	 * @return Devuelve un double con el precio
	 */
	private double calcularPrecioViaje(Tipo tipoVehiculo, int kmRecorridos) {
		double base = 10.0;
	    double multiplicador;

	    switch (tipoVehiculo) {
	        case TURISMO:
	            multiplicador = 0.25;
	            break;
	        case SUV:
	            multiplicador = 0.35;
	            break;
	        case BIPLAZA:
	            multiplicador = 0.30;
	            break;
	        case MONOVOLUMEN:
	            multiplicador = 0.40;
	            break;
	        default:
	        	throw new ResponseStatusException(HttpStatus.CONFLICT, "Tipo de vehículo inválido");
	    }

	    return base + (kmRecorridos * multiplicador);
	}
}
