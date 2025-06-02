package com.xxxiv.service;

import java.util.ArrayList;
import java.util.List;

import com.xxxiv.dto.RutaDTO;
import com.xxxiv.model.Coordinate;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ViajeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final ViajeRepository viajeRepository;

    @Transactional(readOnly = true)
    public RutaDTO obtenerRutaPorViaje(Integer viajeId) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con id=" + viajeId));

        // Extraemos el vehículo para obtener marca y modelo
        Vehiculo vehiculo = viaje.getVehiculo();
        if (vehiculo == null) {
            throw new EntityNotFoundException("El viaje con id=" + viajeId + " no tiene vehículo asociado.");
        }

        // Preparamos el DTO
        RutaDTO dto = new RutaDTO();
        dto.setId(viaje.getId());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());

        // Obtenemos los puntos (convertimos List<Coordinate> a List<double[]>)
        List<double[]> listaPuntos = new ArrayList<>();
        List<Coordinate> coords = viaje.getCods();
        if (coords != null) {
            for (Coordinate c : coords) {
                listaPuntos.add(new double[]{ c.getLat(), c.getLng() });
            }
        }
        dto.setPuntos(listaPuntos);

        return dto;
    }

    @Transactional
    public RutaDTO reemplazarRuta(Integer viajeId, List<double[]> nuevosPuntos) {
        Viaje viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new EntityNotFoundException("Viaje no encontrado con id=" + viajeId));

        Vehiculo vehiculo = viaje.getVehiculo();
        if (vehiculo == null) {
            throw new EntityNotFoundException("El viaje con id=" + viajeId + " no tiene vehículo asociado.");
        }

        // Reconstruimos la lista de Coordinate a partir de los nuevos puntos
        List<Coordinate> listaCoordenadas = new ArrayList<>();
        for (double[] punto : nuevosPuntos) {
            listaCoordenadas.add(new Coordinate(punto[0], punto[1]));
        }

        // Asignamos la nueva ruta y guardamos el viaje
        viaje.setCods(listaCoordenadas);
        viajeRepository.save(viaje);

        // Volvemos a armar el DTO de respuesta
        RutaDTO dto = new RutaDTO();
        dto.setId(viaje.getId());
        dto.setMarca(vehiculo.getMarca());
        dto.setModelo(vehiculo.getModelo());

        // Convertimos List<Coordinate> recién guardada a List<double[]>
        List<double[]> listaPuntosDTO = new ArrayList<>();
        for (Coordinate c : listaCoordenadas) {
            listaPuntosDTO.add(new double[]{ c.getLat(), c.getLng() });
        }
        dto.setPuntos(listaPuntosDTO);

        return dto;
    }
}
