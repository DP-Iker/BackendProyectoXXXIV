package com.xxxiv.service;

import com.xxxiv.dto.UbicacionVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Autowired
    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    // Find all
    public List<Vehiculo> findAll() {
    	return vehiculoRepository.findAll();
    }
    
    // Find by ID
    public Optional<Vehiculo> findById(int id) {
        return vehiculoRepository.findById(id);
    }
    
    // Get location
    public List<UbicacionVehiculosDTO> getUbicaciones() {
    	// Busca todos los vehiculos y los guarda en una lista
    	List<Vehiculo> vehiculosDisponibles = vehiculoRepository.findByEstado(Estado.DISPONIBLE);
    	
    	// Inicializa la lista que va a enviar
    	List<UbicacionVehiculosDTO> ubicacionesVehiculo = new ArrayList<>();
    	
    	// Por cada vehiculo disponible, añade a la lista su id y ubicacion
    	vehiculosDisponibles.forEach(vehiculo -> {
    		// Inicializa la variable y la llena
    		UbicacionVehiculosDTO ubicacionVehiculo = new UbicacionVehiculosDTO();
    		ubicacionVehiculo.setId(vehiculo.getId());
    		ubicacionVehiculo.setLatitud(vehiculo.getLatitud());
    		ubicacionVehiculo.setLongitud(vehiculo.getLongitud());
    		
    		ubicacionesVehiculo.add(ubicacionVehiculo); // Lo agrega a la lista
    	});
    	
    	return ubicacionesVehiculo;
    }
}
