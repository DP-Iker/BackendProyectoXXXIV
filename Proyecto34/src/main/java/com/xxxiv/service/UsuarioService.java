package com.xxxiv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxxiv.model.Usuario;
import com.xxxiv.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	UsuarioRepository usuarioRepository;
	
	public List<Usuario> findAll() {
		return usuarioRepository.findAll();
	}
}
