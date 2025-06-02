package com.xxxiv.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxxiv.model.Viaje;
import com.xxxiv.repository.ViajeRepository;

@Service
public class ViajeService {

	@Autowired
	private ViajeRepository viajeRepository;

	public Viaje finalizarViaje(Integer viajeId, LocalDate fechaFin, Integer kmRecorridos) {
		Viaje viaje = viajeRepository.findById(viajeId).orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

		viaje.finalizarViaje(fechaFin, kmRecorridos);

		return viajeRepository.save(viaje);
	}

	public ViajeService(ViajeRepository viajeRepository) {
		this.viajeRepository = viajeRepository;
	}

	public List<Viaje> findAll() {
		return viajeRepository.findAll();
	}

	public Optional<Viaje> buscarPorId(Integer id) {
		return viajeRepository.findById(id);
	}

	public Viaje save(Viaje viaje) {
		return viajeRepository.save(viaje);
	}

	public void deleteById(Integer id) {
		viajeRepository.deleteById(id);
	}
}
