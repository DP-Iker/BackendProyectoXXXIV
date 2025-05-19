package com.xxxiv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xxxiv.dto.CrearUsuarioDTO;
import com.xxxiv.model.Usuario;
import com.xxxiv.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/usuarios")
public class UsuarioController {
	@Autowired
	UsuarioService usuarioService;

	// GET
	@GetMapping
	public List<Usuario> getUsuarios() {
		return usuarioService.findAll();
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
	
	// POST
	@PostMapping
	public ResponseEntity<Usuario> crearUsuario(@RequestBody @Valid CrearUsuarioDTO dto) {
	    Usuario usuario = usuarioService.crearUsuario(dto.getUsuario(), dto.getContrasenya(), dto.getEmail());
	    return new ResponseEntity<>(usuario, HttpStatus.CREATED);
	}
	
	
	// DELETE
	@DeleteMapping("/{id}")
	public String eliminarUsuario(@PathVariable int id) {
		return usuarioService.eliminarUsuario(id);
	}
}
