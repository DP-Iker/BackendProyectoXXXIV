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

//    public String saveImg(CarnetImageDTO dto) throws IOException {
//        MultipartFile imagen = dto.getImagen();
//
//        if (imagen == null || imagen.isEmpty()) {
//            throw new IOException("Imagen vacía");
//        }
//
//        if (imagen == null || imagen.isEmpty()) {
//            throw new IOException("No se ha enviado ninguna imagen");
//        }
//
//        String originalConRuta = imagen.getOriginalFilename() == null
//                ? ""
//                : imagen.getOriginalFilename();
//        String originalSoloNombre = Paths
//                .get(originalConRuta)
//                .getFileName()
//                .toString();
//
//        String extension = "";
//        int punto = originalSoloNombre.lastIndexOf('.');
//        if (punto >= 0) {
//            extension = originalSoloNombre.substring(punto).toLowerCase();
//            // Ej. ".jpg", ".png"
//        }
//
//
//        String usuario = dto.getUsuario().trim(); // p. ej. "pepito123"
//        if (usuario.isEmpty()) {
//            throw new IOException("El campo 'usuario' no puede estar vacío");
//        }
//
//        long timestamp = System.currentTimeMillis();
//        String nombreArchivo = usuario + "_" + timestamp + extension;
//
//        //        UUID.randomUUID()
//
//
//        //        String nombreArchivo =  "carnet-" + imagen.getOriginalFilename();
//        Path ruta = Paths.get(carpetaImagenes + nombreArchivo);
//        Files.createDirectories(ruta.getParent());
//        Files.copy(imagen.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);
//
//
//        Carnet carnet = carnetRepository.findByUsuarioUsuario(dto.getUsuario())
//                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
//        carnet.setImagenUrl("/imagenes/" + nombreArchivo);
//        carnetRepository.save(carnet);
//
//        return carnet.getImagenUrl();
//    }

    /**
     * 1) Valida que lleguen usuario (String) e imagen (MultipartFile).
     * 2) Si el usuario NO existe en la tabla usuario → error.
     * 3) Si el carnet para ese usuario NO existe en la tabla carnet → lo crea.
     * 4) Guarda la imagen en disco con nombre "<usuario>_<timestamp>.<ext>".
     * 5) Asocia ese nombre al campo imagenUrl del Carnet (creado o existente).
     * 6) Devuelve la URL pública "/api/carnets/{id}/imagen" para que el front la muestre.
     */
    public String saveImg(CarnetImageDTO dto) throws IOException {
        // ——————————— 1) Validar el MultipartFile ———————————
        MultipartFile imagen = dto.getImagen();
        if (imagen == null || imagen.isEmpty()) {
            throw new IOException("No se ha enviado ninguna imagen");
        }

        // ——————————— 2) Extraer nombre original y extensión ———————————
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

        // ——————————— 4) Buscar el objeto Usuario en BD ———————————
        logger.info("Buscando usuario: {}", dto.getUsuario());
        Usuario usuarioEntity = usuarioRepository
                .findByUsuario( dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " +  dto.getUsuario()));
        logger.info("Usuario encontrado: {}", usuarioEntity);

        // ——————————— 5) Construir nombre de fichero deseado ———————————
        long timestamp = System.currentTimeMillis();
        // Por ejemplo: "pepito123_1685604194123.jpg"
        String nombreArchivo =  dto.getUsuario() + "_" + timestamp + extension;

        // ——————————— 6) Preparar la carpeta en disco ———————————
        Path carpetaDestino = Paths.get(carpetaImagenes).toAbsolutePath().normalize();
        if (!Files.exists(carpetaDestino)) {
            Files.createDirectories(carpetaDestino);
        }
        Path rutaDestino = carpetaDestino.resolve(nombreArchivo).normalize();

        // ——————————— 7) Copiar el contenido del MultipartFile a disco ———————————
        Files.copy(imagen.getInputStream(), rutaDestino, StandardCopyOption.REPLACE_EXISTING);

        // ——————————— 8) Buscar o crear el Carnet asociado a ese Usuario ———————————
        //    - Si ya existía un Carnet con ese Usuario, lo recupera.
        //    - Si no existía, crea uno nuevo y lo asocia al Usuario.
        Carnet carnet = carnetRepository
                .findByUsuario(usuarioEntity)
                .orElseGet(() -> {
                    // Aquí creamos un Carnet vacio para ese Usuario:
                    Carnet nuevo = new Carnet();
                    // MapsId usa usuarioEntity.getId() como PK en Carnet
                    nuevo.setUsuarioId(usuarioEntity.getId());
                    nuevo.setUsuario(usuarioEntity);
                    return nuevo;
                });

        // ——————————— 9) Setear el nombre de la imagen en la entidad Carnet ———————————
        //    En BD guardamos solo "pepito123_1685604194123.jpg".
        carnet.setImagenUrl(nombreArchivo);
        carnetRepository.save(carnet);

        // ——————————— 10) Devolver la URL pública que el frontend usará para <img src="…"> ———————————
        // Si tu Controller está anotado con @RequestMapping("/api/carnets"),
        // la ruta queda: GET /api/carnets/{id}/imagen
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

//        if (!optCarnet.isPresent()) {
//            logger.warn("No existe ningún Carnet con id={}", id);
//            // Devuelve 404 porque no hay registro en BBDD
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }

        Carnet carnet = optCarnet.get();

        // 2) Obtener la ruta completa al fichero de imagen
        String nombreFichero = carnet.getImagenUrl();
        Path rutaFichero = Paths.get(carpetaImagenes).resolve(nombreFichero).normalize();
        logger.info("Ruta completa al fichero de imagen: {}", rutaFichero);
        try {
            // 3) Comprobar que el archivo existe y es legible
            if (!Files.exists(rutaFichero) || !Files.isReadable(rutaFichero)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 4) Crear el Resource a partir de la URI del fichero
            UrlResource resource = new UrlResource(rutaFichero.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // 5) Devuelve la ResponseEntity con cabecera Content-Type y el body = resource
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


    /**
     * Actualiza únicamente el campo 'estado' de un carnet dado.
     *
     * @param idCarnet      ID del carnet a actualizar.
     * @param nuevoEstado   String con el nombre del enum (e.g. "APROBADO").
     * @throws jakarta.persistence.EntityNotFoundException   si no existe ningún Carnet con ese id.
     * @throws IllegalArgumentException  si nuevoEstado no coincide con ningún EstadoCarnet.
     */
    @Transactional
    public void actualizarEstado(String usuario, String nuevoEstado) {
        Carnet entidad = carnetRepository.findByUsuarioUsuario(usuario)
                .orElseThrow(() -> new EntityNotFoundException("Carnet no encontrado con id=" + usuario));

        // Convertir el String a enum. Lanza IllegalArgumentException si no es válido.
        EstadoCarnet estadoEnum = EstadoCarnet.valueOf(nuevoEstado);
        entidad.setEstado(estadoEnum);

        carnetRepository.save(entidad);
    }
}
