package com.xxxiv.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.xxxiv.dto.ViajeActualizarDTO;
import com.xxxiv.dto.CrearViajeDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.repository.VehiculoRepository;
import com.xxxiv.repository.ViajeRepository;

@Service
public class ViajeService {

	private UsuarioService usuarioService;
	private VehiculoService vehiculoService;
	private ViajeRepository viajeRepository;
	private VehiculoRepository vehiculoRepository;
	
	
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
	
	public Viaje crearViaje(CrearViajeDTO dto) {
	    Usuario usuario = usuarioService.obtenerUsuarioPorId(dto.getUsuarioId());
	    Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorId(dto.getVehiculoId());

	    if (vehiculo.getEstado() != Estado.DISPONIBLE) {
	    	throw new ResponseStatusException(HttpStatus.CONFLICT, "El vehículo no está disponible para un nuevo viaje");
	    }

	    vehiculo.setEstado(Estado.EN_USO);
	    vehiculo.setLatitud(dto.getLatitud());
	    vehiculo.setLongitud(dto.getLongitud());
	    vehiculo.setLocalidad(dto.getLocalidad());
	    vehiculoRepository.save(vehiculo);

	    Viaje viaje = dto.toEntity();
	    viaje.setUsuario(usuario);
	    viaje.setVehiculo(vehiculo);

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
