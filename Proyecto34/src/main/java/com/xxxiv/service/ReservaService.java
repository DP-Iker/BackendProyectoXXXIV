package com.xxxiv.service;

import com.xxxiv.dto.CrearReservaDTO;
import com.xxxiv.dto.FiltroReservasDTO;
import com.xxxiv.model.Parking;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.EstadoReserva;
import com.xxxiv.repository.ReservaRepository;
import com.xxxiv.specifications.ReservaSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;
    private final ParkingService parkingService;
    private final ViajeService viajeService;
    
    /**
	 * Busca la reserva por ID
	 * 
	 * @param id ID de la reserva
	 * @return Devuelve la reserva, si no da un error 404
	 */
    public Reserva obtenerReservaPorId(int id) {
    	return reservaRepository.findById(id)
    	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva con id "+ id +" no encontrado"));
    }

    /**
     * Busca reservas dependiendo de los filtros
     * 
     * @param filtro Filtro de los campos
     * @param pageable Pageable
     * @return Devueve una Page de reservas
     */
    public Page<Reserva> buscarReservas(FiltroReservasDTO filtro, Pageable pageable) {
		Specification<Reserva> filtrosAplicados = ReservaSpecification.buildSpecification(filtro);
		return reservaRepository.findAll(filtrosAplicados, pageable);
	}
    
    /**
     * Crea una reserva al usuario
     * @param nombreUsuario Nombre del usuario
     * @param dto DTO con los campos necesarios
     * @return Devuelve la Reserva creada
     */
    @Transactional
    public Reserva crearReserva(String nombreUsuario, CrearReservaDTO dto) {
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(nombreUsuario);

        // // Busca si el vehículo existe y está disponible
        Vehiculo vehiculo = vehiculoService.obtenerVehiculoPorId(dto.getVehiculoId());
        if (vehiculo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehículo no encontrado");
        }
        if (vehiculo.getEstado() != Estado.DISPONIBLE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehículo no disponible");
        }

        // Busca si el parquing existe
        Parking parkingRecogida = parkingService.obtenerParkingPorId(dto.getParkingRecogidaId());
        if (parkingRecogida == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parking de recogida no encontrado");
        }

        // Deja el vehiculo en reservado
        vehiculo.setEstado(Estado.RESERVADO);
        vehiculoService.guardarVehiculo(vehiculo);

        // Crea la reserva
        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setVehiculo(vehiculo);
        reserva.setParkingRecogida(parkingRecogida);
        reserva.setEstado(EstadoReserva.PENDIENTE);

        return reservaRepository.save(reserva);
    }
    
    /**
     * Verifica si el propietario de la reserva es el mismo usuario del token
     * 
     * @param nombreUsuario Nombre de usuario
     * @param id ID de la reserva
     * @retun reserva Reserva propia
     */
    private Reserva verificarReservaPropia(String nombreUsuario, int id) {
    	int usuarioId = usuarioService.obtenerUsuarioPorNombre(nombreUsuario).getId();
    	Reserva reserva = obtenerReservaPorId(id);
    	
    	// Mira que la reserva sea del usaurio que ha enviado la petición
    	if (reserva.getUsuario().getId() != usuarioId) {
    		throw new ResponseStatusException(HttpStatus.CONFLICT, "La reserva no te pertenece");
    	}
    	return reserva;
    }
    
    /**
     * Confirma la reserva y pone el coche en uso
     * 
     * @param nombreUsuario Nombre de ususario
     * @param id ID de la reserva
     */
    public void confirmarReserva(String nombreUsuario, int id) {
    	Reserva reserva = verificarReservaPropia(nombreUsuario, id);
    	
    	// Comprueba que esté pendiente la reserva
    	if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Sólo se pueden confirmar reservas en estado PENDIENTE");
        }
    	
    	Vehiculo vehiculo = reserva.getVehiculo();
    	if (vehiculo == null) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehículo asociado a la reserva con id " + reserva.getId() + " no encontrado");
        }
    	
    	// Pone el vehículo en uso
        vehiculo.setEstado(Estado.EN_USO);
        vehiculoService.guardarVehiculo(vehiculo);
        
        // Crea el viaje
        Viaje viaje = viajeService.crearViaje(reserva);

        // Completa los campos de la reserva pendientes
        reserva.setViaje(viaje);
        reserva.setEstado(EstadoReserva.CONFIRMADA);
        reservaRepository.save(reserva);
    }
    
    /**
     * Cancela y libera el vehículo
     * @param reserva Reserva
     */
    private void aplicarCancelacion(Reserva reserva) {
        Vehiculo vehiculo = reserva.getVehiculo();
        
        // Si el vehículo deja de existir
        if (vehiculo == null) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vehículo asociado a la reserva con id " + reserva.getId() + " no encontrado");
        }
        		
        vehiculo.setEstado(Estado.DISPONIBLE);
        vehiculoService.guardarVehiculo(vehiculo);

        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }
    
    /**
     * Cancela la reserva
     * 
     * @param id ID de la reserva
     */
    @Transactional
    public void cancelarReserva(int id) {
    	Reserva reserva = obtenerReservaPorId(id);
    	aplicarCancelacion(reserva);
    }
    
    /**
     * Cancela la reserva si el usuario es el mismo
     * 
     * @param nombreUsuario Nombre de usuario
     * @param id ID de la reserva
     */
    @Transactional
    public void cancelarReservaPropia(String nombreUsuario, int id) {
    	Reserva reserva = verificarReservaPropia(nombreUsuario, id);
    	
    	aplicarCancelacion(reserva);
    }
    
    /**
     * Cancela las reservas pendientes que hace 5 min que se crearon
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void cancelarReservasVencidas() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(30);
        
        // Busca las reservas pendientes que se han pasado del límite
        List<Reserva> vencidas = reservaRepository
            .findByEstadoAndFechaReservaBefore(EstadoReserva.PENDIENTE, limite);
        
        for (Reserva reserva : vencidas) {
            aplicarCancelacion(reserva);
        }
        reservaRepository.saveAll(vencidas);
    }
    
    /**
     * Elimina una reserva
     * @param id ID de la reserva
     */
    public void eliminarReserva(int id) {
    	Reserva reserva = obtenerReservaPorId(id);
    	
        reservaRepository.delete(reserva);
    }
}
