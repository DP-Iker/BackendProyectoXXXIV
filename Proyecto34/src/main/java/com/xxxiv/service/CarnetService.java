package com.xxxiv.service;

import com.xxxiv.dto.CarnetDTO;
import com.xxxiv.dto.CarnetImageDTO;
import com.xxxiv.dto.CarnetSinValidarDTO;
import com.xxxiv.dto.ValidarCarnetDTO;
import com.xxxiv.model.Carnet;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.Vehiculo;
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
    
    /**
     * Crea una lista con todos los carnets que faltan por validar
     * @return Lista con carnets ordenados (de mas antiguos primero)
     */
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

    /**
     * Obtiene el carnet por su ID
     * 
     * @param id ID del carnet
     * @return Devuelve el Carnet
     */
    public Carnet obtenerCarnetPorId(int id) {
    	return carnetRepository.findById(id)
    	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carnet no encontrado"));
    }

    /**
     * Crea una solicitud para validar el carnet
     * 
     * @param usuario Usuario
     * @param imagen Imagen del carnet
     * @return Devuelve el carnet con los campos que se han rellenado
     * @throws IOException
     */
    public CarnetSinValidarDTO crearSolicitud(Usuario usuario, MultipartFile imagen) throws IOException {
    	if (carnetRepository.existsByUsuarioId(usuario.getId())) {
    		throw new ResponseStatusException(HttpStatus.CONFLICT, "Este usuario ya tiene un carnet");
        }
    	
    	if (imagen.isEmpty() || !imagen.getContentType().startsWith("image/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Archivo no válido");
        }

        // Guardar imagen
        String rutaImagen = guardarImagenLocal(imagen, usuario.getId());
        
        Carnet carnet = new Carnet();
        carnet.setUsuario(usuario);
        carnet.setEstaValidado(false);
        carnet.setImagenUrl(rutaImagen);
        carnet.setFechaSolicitud(LocalDateTime.now());
    	
    	carnetRepository.save(carnet);
    	
    	return new CarnetSinValidarDTO(carnet);
    }
    
    /**
     * Valida el carnet con los campos correspondientes
     * 
     * @param id ID del carnet
     * @param dto Campos
     */
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
        // Crear el directorio si no existe
        File directorio = new File(uploadDir);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Obtener la extensión
        String extension = Optional.ofNullable(archivo.getOriginalFilename())
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf(".")))
                .orElse(".jpg");

        // Nombre del archivo
        String nombreArchivo = "carnet_usuario_" + usuarioId + extension;

        // Ruta completa
        Path ruta = Paths.get(uploadDir).resolve(nombreArchivo);
        Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

        // Devolver la ruta pública accesible desde el frontend
        return "/uploads/carnets/" + nombreArchivo;
    }
    
    /**
     * Elimina el carnet por ID
     * 
     * @param id ID del carnet
     */
    public void eliminarCarnet(int id) {
		Carnet carnet = obtenerCarnetPorId(id);
		
		carnetRepository.delete(carnet);
	}
}
