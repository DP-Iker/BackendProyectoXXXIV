package com.xxxiv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.xxxiv.dto.FiltroVehiculosDTO;
import com.xxxiv.dto.UbicacionVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Tipo;
import com.xxxiv.repository.VehiculoRepository;
import com.xxxiv.specifications.VehiculoSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VehiculoService {
	
	private final VehiculoRepository vehiculoRepository;
	private final WebClientService webClientService;

	/**
	 * Busca los vehículos según el filtro y los devuelve en página
	 * 
	 * @param filtro   Campos por los que se puede filtrar
	 * @param pageable Página
	 * @return Página con los elementos
	 */
	public Page<Vehiculo> buscarVehiculos(FiltroVehiculosDTO filtro, Pageable pageable) {
		Specification<Vehiculo> filtrosAplicados = VehiculoSpecification.buildSpecification(filtro);
		return vehiculoRepository.findAll(filtrosAplicados, pageable);
	}

	/**
	 * Busca por ID el vehículo
	 * 
	 * @param id ID del vehículo
	 * @return Devuelve el vehiculo o un 404
	 */
	public Optional<Vehiculo> buscarPorId(int id) {
	    return vehiculoRepository.findById(id);
	}

	/**
	 * Busca todas las ubicaciones donde hay vehículos con el id del vehículo
	 * 
	 * @return Devuelve el ID, su latitud y longitud y su localidad
	 */
	public List<UbicacionVehiculosDTO> getUbicaciones(Tipo tipo) {
		// Busca todos los vehiculos y los guarda en una lista
		List<Vehiculo> vehiculosDisponibles = vehiculoRepository.findByEstado(Estado.DISPONIBLE);
		
		// Si ha filtrado por el tipo, solo devuelve los disponibles de ese tipo
		if (tipo != null) {
			vehiculosDisponibles = vehiculoRepository.findByEstadoAndTipo(Estado.DISPONIBLE, tipo);
		} else {
			vehiculosDisponibles = vehiculoRepository.findByEstado(Estado.DISPONIBLE);
		}

		// Inicializa la lista que va a enviar
		List<UbicacionVehiculosDTO> ubicacionesVehiculo = new ArrayList<>();

		// Por cada vehiculo disponible, añade a la lista su id y ubicacion
		vehiculosDisponibles.forEach(vehiculo -> {
			// Inicializa la variable y la llena
			UbicacionVehiculosDTO ubicacionVehiculo = new UbicacionVehiculosDTO();
			ubicacionVehiculo.setId(vehiculo.getId());
			ubicacionVehiculo.setLatitud(vehiculo.getLatitud());
			ubicacionVehiculo.setLongitud(vehiculo.getLongitud());
			ubicacionVehiculo.setLocalidad(vehiculo.getLocalidad());

			ubicacionesVehiculo.add(ubicacionVehiculo); // Lo agrega a la lista
		});

		return ubicacionesVehiculo;
	}

	/**
	 * Busca todas las localidades dónde hay un vehículo disponible
	 * 
	 * @return Devuelve una lista de localidades
	 */
	public List<String> getLocalidadesDisponibles() {
		return vehiculoRepository.buscarLocalidadesDisponibles(Estado.DISPONIBLE);
	}
	
	/**
	 * Actualiza la ubicacion del vehículo (Consulta a una API externa la localidad más cercana)
	 * @param id ID del vehículo
	 * @param latitud Nueva latitud
	 * @param longitud Nueva longitud
	 * @return Devuelve un String si ha funcionado
	 */
	public String actualizarUbicacion(int id, double latitud, double longitud) {
		String localidad = webClientService.obtenerLocalidad(latitud, longitud);
		
		Vehiculo vehiculo = buscarPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));;
		
		// Modifica el vehículo
		vehiculo.setLatitud(latitud);
		vehiculo.setLongitud(longitud);
		vehiculo.setLocalidad(localidad);
		
		vehiculoRepository.save(vehiculo);
		
		return "Ubicación del vehículo "+ id +" actualizada";
	}
	
	

}
