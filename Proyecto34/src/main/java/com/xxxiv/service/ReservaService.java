package com.xxxiv.service;

import com.xxxiv.dto.ReservaDetalleDTO;
import com.xxxiv.model.Parking;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<ReservaDetalleDTO> obtenerReservasPorUsuario(String nombreUsuario) {
        List<Reserva> reservas = reservaRepository.findByUsuarioUsuarioOrderByFechaReservaDesc(nombreUsuario);

        return reservas.stream()
                .map(this::convertirADetalleDTO)
                .collect(Collectors.toList());
    }

    private ReservaDetalleDTO convertirADetalleDTO(Reserva r) {
        Viaje v = r.getViaje();
        Vehiculo veh = r.getVehiculo();
        Parking pr = r.getParkingRecogida();
        Parking pd = r.getParkingDevolucion();

        return new ReservaDetalleDTO(
                r.getId(),
                r.getUsuario().getId(),
                v.getId(),
                v.getFechaInicio(),
                v.getFechaFin(),
                v.getKmRecorridos(),
                veh.getId(),
                veh.getMarca(),
                veh.getModelo(),
                pr.getId(),
                pr.getName(),   // ejemplo de campo “name” en Parking
                pd.getId(),
                pd.getName(),   // idem
                r.getFechaReserva(),
                r.getEstado()
        );
    }
}
