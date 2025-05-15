package com.xxxiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxxiv.model.Vehiculo;
import com.xxxiv.repository.VehiculoRepository;

@Service
public class VehiculoService {
	@Autowired
	VehiculoRepository vehiculoReposirtory;
	
	public List<Vehiculo> findAll() {
		return vehiculoReposirtory.findAll();
	}
}
