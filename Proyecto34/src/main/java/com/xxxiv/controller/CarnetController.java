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

    // POST
//    @PostMapping("/saveimg")
//    @Operation(summary = "Guardar Imagen de Carnet", description = "Guarda la Imagen del Carnet para posterior validacion")
//    public ResponseEntity<String> saveImg(@ModelAttribute CarnetImageDTO dto) {
//        try {
//            String ruta = carnetService.saveImg(dto);
//            return ResponseEntity.ok("Imagen guardada en: " + ruta);
//            // Si no consigue hacer
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @PostMapping("/saveimg")
    @Operation(summary = "Guardar Imagen de Carnet", description = "Guarda la Imagen del Carnet para posterior validación")
    public ResponseEntity<String> saveImg(@ModelAttribute CarnetImageDTO dto) throws IOException {
//        // 1) dto.getUsuario(): cadena String con nombre de usuario
//        // 2) dto.getImagen(): MultipartFile con la foto subida
//
//        if (dto.getUsuario() == null || dto.getUsuario().isBlank()) {
//            return ResponseEntity.badRequest().body("El usuario es obligatorio");
//        }
//        if (dto.getImagen() == null || dto.getImagen().isEmpty()) {
//            return ResponseEntity.badRequest().body("No se ha enviado ninguna imagen");
//        }

       return ResponseEntity.ok(carnetService.saveImg(dto));
//
//        // Luego buscas al Usuario en BD, guardas el archivo, actualizas Carnet, etc.
//        // ...
//        return ResponseEntity.ok("Imagen guardada correctamente");
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
