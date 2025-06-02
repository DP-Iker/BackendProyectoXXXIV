package com.xxxiv.service;

import com.xxxiv.dto.CarnetDTO;
import com.xxxiv.dto.CarnetImageDTO;
import com.xxxiv.model.Carnet;
import com.xxxiv.model.Usuario;
import com.xxxiv.model.enums.EstadoCarnet;
import com.xxxiv.repository.CarnetRepository;
import com.xxxiv.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CarnetService {
    private final String carpetaImagenes = "C:/Users/jorgi/Desktop/PryectoFinal/imagenes/carnets/";
//    private final String carpetaImagenes = "C:/Users/jorgi/IdeaProjects/BackendProyectoXXXIV/Proyecto34/imagenes/carnets/";

    private static final Logger logger = LoggerFactory.getLogger(CarnetService.class);
    private final CarnetRepository carnetRepository;
    private final UsuarioRepository usuarioRepository;

    public CarnetService(CarnetRepository carnetRepository, UsuarioRepository usuarioRepository) {
        this.carnetRepository = carnetRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Carnet> findAll() {
        return carnetRepository.findAll();
    }

    public Optional<Carnet> findById(Integer usuarioId) {
        return carnetRepository.findById(usuarioId);
    }

    public Carnet save(Carnet carnet) {
        return carnetRepository.save(carnet);
    }

    public void deleteById(Integer usuarioId) {
        carnetRepository.deleteById(usuarioId);
    }

    public String saveImg(CarnetImageDTO dto) throws IOException {
        MultipartFile imagen = dto.getImagen();
        if (imagen == null || imagen.isEmpty()) {
            throw new IOException("No se ha enviado ninguna imagen");
        }

        String originalConRuta = imagen.getOriginalFilename() == null
                ? ""
                : imagen.getOriginalFilename();
        String originalSoloNombre = Paths
                .get(originalConRuta)
                .getFileName()
                .toString();  // e.g. "foto.jpg"
        String extension = "";
        int punto = originalSoloNombre.lastIndexOf('.');
        if (punto >= 0) {
            extension = originalSoloNombre.substring(punto).toLowerCase(); // ".jpg", ".png"
        }

        logger.info("Buscando usuario: {}", dto.getUsuario());
        Usuario usuarioEntity = usuarioRepository
                .findByUsuario( dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " +  dto.getUsuario()));
        logger.info("Usuario encontrado: {}", usuarioEntity);

        long timestamp = System.currentTimeMillis();
        String nombreArchivo =  dto.getUsuario() + "_" + timestamp + extension;

        Path carpetaDestino = Paths.get(carpetaImagenes).toAbsolutePath().normalize();
        if (!Files.exists(carpetaDestino)) {
            Files.createDirectories(carpetaDestino);
        }
        Path rutaDestino = carpetaDestino.resolve(nombreArchivo).normalize();

        Files.copy(imagen.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

        Carnet carnet = carnetRepository
                .findByUsuario(usuarioEntity)
                .orElseGet(() -> {
                    Carnet nuevo = new Carnet();
                    nuevo.setUsuarioId(usuarioEntity.getId());
                    nuevo.setUsuario(usuarioEntity);
                    return nuevo;
                });


        carnet.setImagenUrl(nombreArchivo);
        carnetRepository.save(carnet);

        return "/api/carnets/" + carnet.getUsuarioId() + "/imagen";
    }


    public List<CarnetDTO> obtenerUsuariosConImagen() {
        List<Carnet> carnets = carnetRepository.findByEstadoIn(Collections.singleton(EstadoCarnet.PENDIENTE));

        return carnets.stream().map(carnet -> {
            CarnetDTO dto = new CarnetDTO();
            dto.setUsuario(carnet.getUsuario().getUsuario());
            dto.setNombre(carnet.getNombre());
            dto.setDni(carnet.getDni());
            dto.setFechaNacimiento(carnet.getFechaNacimiento());
            dto.setEstado(carnet.getEstado());
            dto.setImagenUrl(
                    carnet.getImagenUrl() != null
                            ? "/carnets/" + carnet.getUsuarioId() + "/imagen"
                            : null
            );

            return dto;
        }).collect(Collectors.toList());
    }


    public ResponseEntity<Resource> obtenerImagen(Integer id) {
        logger.info("Buscando Carnet con id={}", id);
        Optional<Carnet> optCarnet = carnetRepository.findById(id);

        Carnet carnet = optCarnet.get();

        String nombreFichero = carnet.getImagenUrl();
        Path rutaFichero = Paths.get(carpetaImagenes).resolve(nombreFichero).normalize();
        logger.info("Ruta completa al fichero de imagen: {}", rutaFichero);
        try {
            if (!Files.exists(rutaFichero) || !Files.isReadable(rutaFichero)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            UrlResource resource = new UrlResource(rutaFichero.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String contentType = Files.probeContentType(rutaFichero);
            if (contentType == null) {
                contentType = "application/octet-stream"; // fallback por seguridad
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body((Resource) resource);

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Transactional
    public void actualizarEstado(String usuario, String nuevoEstado) {
        Carnet entidad = carnetRepository.findByUsuarioUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Carnet no encontrado con id=" + usuario));

        EstadoCarnet estadoEnum = EstadoCarnet.valueOf(nuevoEstado);
        entidad.setEstado(estadoEnum);

        carnetRepository.save(entidad);
    }
}
