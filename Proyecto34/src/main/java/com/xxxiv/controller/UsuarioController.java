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

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
	@Autowired
	UsuarioService usuarioService;

	// GET
	@GetMapping
	@Operation(summary = "Devuelve todos los usuarios", description = "Devuelve todos los usuarios que hay en la BD")
	public List<Usuario> getUsuarios() {
		return usuarioService.findAll();
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Devuelve al usuario por ID", description = "Devuelve todos los datos del usuario de esa ID que hay en la BD")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable int id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
	
	// POST
	@PostMapping
	@Operation(summary = "Crea un usuario", description = "Crea un usuario si le envias un nombre de usuario único, una contraseña y un email único")
	public ResponseEntity<Usuario> crearUsuario(@RequestBody @Valid CrearUsuarioDTO dto) {
	    Usuario usuario = usuarioService.crearUsuario(dto.getUsuario(), dto.getContrasenya(), dto.getEmail());
	    return new ResponseEntity<>(usuario, HttpStatus.CREATED);
	}
	
	
	// DELETE
	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina al usuario por ID", description = "Elimina al usuario de la BD con el ID")
	public String eliminarUsuario(@PathVariable int id) {
		return usuarioService.eliminarUsuario(id);
	}
}
