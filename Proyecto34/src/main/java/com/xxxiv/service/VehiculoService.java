package com.xxxiv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.xxxiv.dto.VehiculoEnUsoDTO;
import com.xxxiv.model.SeguimientoRuta;
import com.xxxiv.model.Viaje;
import com.xxxiv.repository.SeguimientoRutaRepository;
import com.xxxiv.repository.ViajeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.FiltroVehiculosDTO;
import com.xxxiv.dto.UbicacionVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Localidad;
import com.xxxiv.model.enums.Tipo;
import com.xxxiv.repository.VehiculoRepository;
import com.xxxiv.specifications.VehiculoSpecification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VehiculoService {
	private final VehiculoRepository vehiculoRepository;
	private final ViajeRepository viajeRepository;
	private final SeguimientoRutaRepository rutaRepository;

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
	public List<Localidad> getLocalidadesDisponibles() {
		return vehiculoRepository.buscarLocalidadesDisponibles(Estado.DISPONIBLE);
	}

	public Vehiculo save(Vehiculo vehiculo) {
		return vehiculoRepository.save(vehiculo);
	}

	public boolean actualizarUbicacion(int idVehiculo, Double latitud, Double longitud, Localidad localidad) {
		if (latitud == null || longitud == null)
			return false;

		Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado"));

		vehiculo.setLatitud(latitud);
		vehiculo.setLongitud(longitud);

		if (localidad != null) {
			vehiculo.setLocalidad(localidad);
		}

		vehiculoRepository.save(vehiculo);
		return true;
	}


	public List<VehiculoEnUsoDTO> obtenerVehiculosEnUsoConRuta() {

		// Paso 1: Obtener la lista de viajes que están en curso (fechaFin == null)
		List<Viaje> viajesEnCurso = viajeRepository.findByFechaFinIsNull();

		// Paso 2: Mapear cada viaje a su DTO con vehículo y ruta
		List<VehiculoEnUsoDTO> resultado = new ArrayList<>();

		for (Viaje viaje : viajesEnCurso) {
			// Paso 3: Obtener el vehículo del viaje
			Vehiculo vehiculo = viaje.getVehiculo();

			// Paso 4: Consultar los puntos de ruta ordenados por índice para este viaje
			List<SeguimientoRuta> puntosRuta = rutaRepository.findByViajeIdOrderByIdPuntoIndexAsc(viaje.getId());

			// Paso 5: Mapear cada punto de ruta a un DTO simple con latitud, longitud y velocidad
			List<RutaPuntoDTO> rutaDTO = new ArrayList<>();
			for (SeguimientoRuta punto : puntosRuta) {
				RutaPuntoDTO dto = new RutaPuntoDTO(punto.getLatitud(), punto.getLongitud(), punto.getVelocidad());
				rutaDTO.add(dto);
			}

			// Paso 6: Construir el DTO final con los datos del vehículo y su ruta
			VehiculoEnUsoDTO vehiculoDTO = new VehiculoEnUsoDTO(
					vehiculo.getId(),
					vehiculo.getMarca(),
					vehiculo.getModelo(),
					rutaDTO
			);

			// Paso 7: Añadir el DTO a la lista resultado
			resultado.add(vehiculoDTO);
		}

		// Paso 8: Devolver la lista de vehículos en uso con sus rutas
		return resultado;
	}
}
