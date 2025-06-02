package com.xxxiv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.xxxiv.dto.CrearVehiculoDTO;
import com.xxxiv.dto.EditarVehiculoDTO;
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
	private final LocalidadService webClientService;

	// GET
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
	 * Busca todas las localidades dónde hay un vehículo disponible
	 * 
	 * @return Devuelve una lista de localidades
	 */
	public List<String> getMarcasDisponibles() {
		return vehiculoRepository.buscarMarcasDisponibles(Estado.DISPONIBLE);
	}
	
	// POST
	/**
	 * Crea un vehículo
	 * 
	 * @param dto DTO con los campos para crear un vehículo
	 * @return Devuelve el vehículo
	 */
	public Vehiculo crearVehiculo(CrearVehiculoDTO dto, String imagenUrl) {
	    Vehiculo vehiculo = new Vehiculo();
	    vehiculo.setMarca(dto.getMarca());
	    vehiculo.setModelo(dto.getModelo());
	    vehiculo.setKilometraje(dto.getKilometraje());
	    vehiculo.setUltimaRevision(dto.getUltimaRevision());
	    vehiculo.setAutonomia(dto.getAutonomia());
	    vehiculo.setEstado(dto.getEstado() != null ? dto.getEstado() : Estado.EN_MANTENIMIENTO);
	    vehiculo.setLatitud(dto.getLatitud());
	    vehiculo.setLongitud(dto.getLongitud());
	    vehiculo.setLocalidad(dto.getLocalidad());
	    vehiculo.setPuertas(dto.getPuertas());
	    vehiculo.setTipo(dto.getTipo());
	    vehiculo.setEsAccesible(dto.isEsAccesible());
	    
	    vehiculo.setImagen(imagenUrl);

	    return vehiculoRepository.save(vehiculo);
	}

	
	// PATCH
	public void editarVehiculo(int id, EditarVehiculoDTO dto, String imagenUrl) {
		Vehiculo vehiculo = buscarPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

	    if (dto.getMarca() != null) vehiculo.setMarca(dto.getMarca());
	    if (dto.getModelo() != null) vehiculo.setModelo(dto.getModelo());
	    if (dto.getKilometraje() != null) vehiculo.setKilometraje(dto.getKilometraje());
	    if (dto.getUltimaRevision() != null) vehiculo.setUltimaRevision(dto.getUltimaRevision());
	    if (dto.getAutonomia() != null) vehiculo.setAutonomia(dto.getAutonomia());
	    if (dto.getEstado() != null) vehiculo.setEstado(dto.getEstado());
	    if (dto.getLatitud() != null) vehiculo.setLatitud(dto.getLatitud());
	    if (dto.getLongitud() != null) vehiculo.setLongitud(dto.getLongitud());
	    if (dto.getLocalidad() != null) vehiculo.setLocalidad(dto.getLocalidad());
	    if (dto.getTipo() != null) vehiculo.setTipo(dto.getTipo());
	    if (dto.getPuertas() != null) vehiculo.setPuertas(dto.getPuertas());
	    if (dto.getEsAccesible() != null) vehiculo.setEsAccesible(dto.getEsAccesible());
	    if (imagenUrl != null) vehiculo.setImagen(imagenUrl);

	    vehiculoRepository.save(vehiculo);
	}

	
	/**
	 * Actualiza la ubicacion del vehículo (Consulta a una API externa la localidad más cercana)
	 * 
	 * @param id ID del vehículo
	 * @param latitud Nueva latitud
	 * @param longitud Nueva longitud
	 * @return Devuelve un String si ha funcionado
	 */
	public String actualizarUbicacion(int id, double latitud, double longitud) {
		String localidad = webClientService.obtenerLocalidad(latitud, longitud);
		
		Vehiculo vehiculo = buscarPorId(id)
				.orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));
		
		// Modifica el vehículo
		vehiculo.setLatitud(latitud);
		vehiculo.setLongitud(longitud);
		vehiculo.setLocalidad(localidad);
		
		vehiculoRepository.save(vehiculo);
		
		return "Ubicación del vehículo "+ id +" actualizada";
	}
}
