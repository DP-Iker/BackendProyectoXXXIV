package com.xxxiv.controller;

import com.xxxiv.dto.CarnetDTO;
import com.xxxiv.dto.CarnetImageDTO;
import com.xxxiv.dto.LoginResponseDTO;
import com.xxxiv.dto.LoginUsuarioDTO;
import com.xxxiv.model.Carnet;
import com.xxxiv.service.CarnetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/carnets")
@SecurityRequirement(name = "bearerAuth")
public class CarnetController {

    @Autowired
    CarnetService carnetService;

    @GetMapping
    public List<Carnet> getAllCarnets() {
        return carnetService.findAll();
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Carnet> getCarnetById(@PathVariable Integer usuarioId) {
        Optional<Carnet> carnetOpt = carnetService.findById(usuarioId);
        return carnetOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/saveimg")
    @Operation(summary = "Guardar Imagen de Carnet", description = "Guarda la Imagen del Carnet para posterior validación")
    public ResponseEntity<String> saveImg(@ModelAttribute CarnetImageDTO dto) throws IOException {

       return ResponseEntity.ok(carnetService.saveImg(dto));
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable Integer id) {
        return carnetService.obtenerImagen(id);
    }

    @PostMapping("/conimg")
    public ResponseEntity<List<CarnetDTO>> listarCarnetsConImagen() {
        return ResponseEntity.ok(carnetService.obtenerUsuariosConImagen());
    }

    @PutMapping("/{usuario}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable String usuario,
            @RequestBody Map<String, String> payload
    ) {
        String nuevoEstadoStr = payload.get("estado");
        if (nuevoEstadoStr == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            carnetService.actualizarEstado(usuario, nuevoEstadoStr);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException ex) {
            // Si el valor de estado no coincide con ningún enum
            return ResponseEntity.badRequest().build();
        }
    }
}
