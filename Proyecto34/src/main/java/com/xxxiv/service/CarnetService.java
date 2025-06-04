package com.xxxiv.service;

import com.xxxiv.dto.CarnetDTO;
import com.xxxiv.dto.CarnetImageDTO;
import com.xxxiv.dto.CarnetSinValidarDTO;
import com.xxxiv.dto.ValidarCarnetDTO;
import com.xxxiv.model.Carnet;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.enums.EstadoCarnet;
import com.xxxiv.repository.CarnetRepository;
import com.xxxiv.repository.UsuarioRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CarnetService {
	
	@Value("${file.upload-dir}")
    private String uploadDir;
    private final CarnetRepository carnetRepository;
    
    public List<CarnetSinValidarDTO> obtenerCarnetsSinValidarOrdenados() {
        List<Carnet> carnetsSinValidar = carnetRepository.findByEstaValidadoFalseOrderByFechaSolicitudAsc();

        return carnetsSinValidar.stream().map(carnet -> {
            CarnetSinValidarDTO dto = new CarnetSinValidarDTO();
            dto.setUsuarioId(carnet.getUsuario().getId());
            dto.setImagenUrl(carnet.getImagenUrl());
            dto.setFechaSolicitud(carnet.getFechaSolicitud());
            return dto;
        }).collect(Collectors.toList());
    }

    public Carnet obtenerCarnetPorId(int id) {
    	return carnetRepository.findById(id)
    	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carnet no encontrado"));
    }

    public Carnet crearSolicitud(Usuario usuario, MultipartFile imagen) throws IOException {
    	if (imagen.isEmpty() || !imagen.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Archivo inválido");
        }

        // Guardar imagen
        String rutaImagen = guardarImagenLocal(imagen, usuario.getId());
        
        Carnet carnet = new Carnet();
        carnet.setUsuario(usuario);
        carnet.setEstaValidado(false);
        carnet.setImagenUrl(rutaImagen);
        carnet.setFechaSolicitud(LocalDateTime.now());
    	
    	return carnetRepository.save(carnet);
    }
    
    public void validarCarnet(int id, ValidarCarnetDTO dto) {
    	Carnet carnet = obtenerCarnetPorId(id);
    	
    	carnet.setDni(dto.getDni());
        carnet.setNombre(dto.getNombre());
        carnet.setApellido(dto.getApellido());
        carnet.setFechaNacimiento(dto.getFechaNacimiento());
        carnet.setFechaEmision(dto.getFechaEmision());
        carnet.setFechaCaducidad(dto.getFechaCaducidad());
        carnet.setEstaValidado(true);

        // Guarda los cambios
        carnetRepository.save(carnet);
    }
	
    /**
     * Guarda la imagen en la carpeta local
     * 
     * @param archivo Imagen
     * @param usuarioId Id del usuario
     * @return Devuelve la ruta a la imagen
     * @throws IOException
     */
    private String guardarImagenLocal(MultipartFile archivo, Integer usuarioId) throws IOException {
        File directorio = new File(uploadDir);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        String extension = Optional.ofNullable(archivo.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse(".jpg");

        String nombreArchivo = "carnet_usuario_" + usuarioId + extension;
        Path ruta = Paths.get(uploadDir).resolve(nombreArchivo);

        Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

        return ruta.toAbsolutePath().toString();
    }
}
