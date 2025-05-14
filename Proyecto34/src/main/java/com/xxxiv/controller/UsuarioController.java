package com.xxxiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.model.Usuario;
import com.xxxiv.service.UsuarioService;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping(value = "/api")
public class UsuarioController {
	@Autowired
	UsuarioService usuarioSrevice;

	@RequestMapping(value = "/usuarios")
	public List<Usuario> getUsuarios() {
		return usuarioSrevice.findAll();
	}
}
