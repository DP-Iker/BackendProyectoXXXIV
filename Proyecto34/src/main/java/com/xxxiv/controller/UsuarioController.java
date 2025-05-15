package com.xxxiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.service.UsuarioService;
import com.xxxiv.service.VehiculoService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping(value = "/api")
public class UsuarioController {
	@Autowired
	UsuarioService usuarioService;

	@RequestMapping(value = "/usuarios")
	public List<Usuario> getUsuarios() {
		return usuarioService.findAll();
	}
	
	@Autowired
	VehiculoService vehiculoService;
	
	@RequestMapping(value = "/vehiculos")
	public List<Vehiculo> getVehiculos() {
		return vehiculoService.findAll();
	}
}
