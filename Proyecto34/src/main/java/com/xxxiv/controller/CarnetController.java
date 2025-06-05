package com.xxxiv.controller;

import com.xxxiv.dto.CarnetSinValidarDTO;
import com.xxxiv.dto.ValidarCarnetDTO;
import com.xxxiv.model.Carnet;
import com.xxxiv.model.Usuario;
import com.xxxiv.service.CarnetService;
import com.xxxiv.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carnets")
@SecurityRequirement(name = "bearerAuth")
public class CarnetController {

	@Value("${file.upload-dir}")
    private String uploadDir;
	private final CarnetService carnetService;
	private final UsuarioService usuarioService;

	// GET
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<List<CarnetSinValidarDTO>> getCarnetsSinValidar() {
	    List<CarnetSinValidarDTO> carnets = carnetService.obtenerCarnetsSinValidarOrdenados();
	    return ResponseEntity.ok(carnets);
	}
	
	@GetMapping("{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Carnet> getCarnet(@PathVariable int id) {
	    Carnet carnet = carnetService.obtenerCarnetPorId(id);
	    
	    return ResponseEntity.ok(carnet);
	}
	
	@GetMapping("/imagen/{nombreArchivo:.+}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Resource> obtenerImagen(@PathVariable String nombreArchivo) throws IOException {
	    Path rutaImagen = Paths.get(uploadDir).resolve(nombreArchivo).normalize();
	    Resource recurso = new UrlResource(rutaImagen.toUri());

	    if (!recurso.exists() || !recurso.isReadable()) {
	        return ResponseEntity.notFound().build();
	    }

	    // Detectar content type dinámicamente
	    String contentType = Files.probeContentType(rutaImagen);
	    if (contentType == null) {
	        contentType = "application/octet-stream";
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .body(recurso);
	}
	
	// POST
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<CarnetSinValidarDTO> crearSolicitud(Authentication authentication, @RequestParam MultipartFile imagen) throws IOException {
		Usuario usuario = usuarioService.obtenerUsuarioPorNombre(authentication.getName());
		try {
			CarnetSinValidarDTO carnet = carnetService.crearSolicitud(usuario, imagen);
			return new ResponseEntity<>(carnet, HttpStatus.CREATED);

		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	// PATCH
	@PatchMapping("/validar/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	@SecurityRequirement(name = "bearerAuth")
	public ResponseEntity<Void> validarCarnet(@PathVariable int id, @RequestBody @Valid ValidarCarnetDTO dto) {
		carnetService.validarCarnet(id, dto);
		
		return ResponseEntity.noContent().build();
	}
	
	// DELETE
	@DeleteMapping("{id}")
	@SecurityRequirement(name = "bearerAuth")
	@PreAuthorize("hasRole('ADMIN')")
	@Operation(summary = "Elimina un vehículo", description = "Elimina el vehículo por ID")
	public ResponseEntity<Void> eliminarVehiculo(@PathVariable int id) {
		carnetService.eliminarCarnet(id);
		
		return ResponseEntity.noContent().build();
	}
}
