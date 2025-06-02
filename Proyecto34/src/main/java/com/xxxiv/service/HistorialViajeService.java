package com.xxxiv.service;

import com.xxxiv.dto.HistorialViajeDTO;
import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ViajeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialViajeService {

    private final ViajeRepository viajeRepository;

    public HistorialViajeService(ViajeRepository viajeRepository) {
        this.viajeRepository = viajeRepository;
    }


    public List<HistorialViajeDTO> obtenerHistorialPorUsuario(String nombreUsuario) {
        List<Viaje> viajesCompletados = viajeRepository
                .findByUsuarioUsuarioAndFechaFinIsNotNullOrderByFechaInicioDesc(nombreUsuario);

        return viajesCompletados.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private HistorialViajeDTO convertirADTO(Viaje viaje) {
        return new HistorialViajeDTO(
                viaje.getId(),
                (viaje.getVehiculo() != null) ? viaje.getVehiculo().getId() : null,
                viaje.getFechaInicio(),
                viaje.getFechaFin(),
                viaje.getKmRecorridos()
        );
    }
}
