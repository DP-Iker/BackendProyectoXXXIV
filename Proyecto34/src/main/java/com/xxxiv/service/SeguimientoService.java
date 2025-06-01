package com.xxxiv.service;

import com.xxxiv.model.SeguimientoRuta;
import com.xxxiv.repository.SeguimientoRutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeguimientoService {

    @Autowired
    private SeguimientoRutaRepository rutaRepository;

    public List<SeguimientoRuta> obtenerRutaPorViaje(Integer viajeId) {
        return rutaRepository.findByViajeIdOrderByIdPuntoIndexAsc(viajeId);
    }

    public void guardarRuta(Integer viajeId, List<double[]> coordenadas, List<Double> velocidades) {
        List<SeguimientoRuta> rutas = new ArrayList<>();
        for (int i = 0; i < coordenadas.size(); i++) {
            double[] coord = coordenadas.get(i);
            double velocidad = velocidades.get(i);
            rutas.add(new SeguimientoRuta(viajeId, i, coord[0], coord[1], velocidad));
        }
        rutaRepository.saveAll(rutas);
    }
}
