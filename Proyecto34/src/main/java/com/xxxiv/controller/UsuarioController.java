package com.xxxiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.model.Usuario;
import com.xxxiv.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/usuarios")
public class UsuarioController {
	@Autowired
	UsuarioService usuarioService;

	@GetMapping
	public List<Usuario> getUsuarios() {
		return usuarioService.findAll();
	}
}
