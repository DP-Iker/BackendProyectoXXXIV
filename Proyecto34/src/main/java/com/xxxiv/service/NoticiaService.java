package com.xxxiv.service;

import com.xxxiv.dto.FiltroNoticiasDTO;
import com.xxxiv.dto.FiltroUsuariosDTO;
import com.xxxiv.model.Noticia;
import com.xxxiv.model.Usuario;
import com.xxxiv.repository.NoticiaRepository;
import com.xxxiv.repository.UsuarioRepository;
import com.xxxiv.specifications.NoticiaSpecification;
import com.xxxiv.specifications.UsuarioSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NoticiaService {
    @Autowired
    NoticiaRepository noticiaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public NoticiaService(NoticiaRepository noticiaRepository, UsuarioRepository usuarioRepository) {
        this.noticiaRepository = noticiaRepository;
        this.usuarioRepository = usuarioRepository;
    }


    /**
     * Crea una nueva noticia. Valida que el usuario exista primero.
     */
    public Noticia crearNoticia(Noticia noticia, String usuarioUsername) {
        // Comprueba que el usuario existe (si no, lanza excepción)
        Usuario usuario = usuarioRepository.findByUsuario(usuarioUsername)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id=" + usuarioUsername));

        noticia.setUsuario(usuario);
        return noticiaRepository.save(noticia);
    }

    public Page<Noticia> buscarNoticias(FiltroNoticiasDTO filtro, Pageable pageable) {
        Specification<Noticia> filtrosAplicados = NoticiaSpecification.buildSpecification(filtro);
        return noticiaRepository.findAll(filtrosAplicados, pageable);
    }

    /**
     * Recupera todas las noticias.
     */
    @Transactional(readOnly = true)
    public List<Noticia> listarNoticias() {
        return noticiaRepository.findAll();
    }

    /**
     * Recupera una noticia por su ID. Si no existe, lanza EntityNotFoundException.
     */
    @Transactional(readOnly = true)
    public Noticia obtenerPorId(Integer id) {
        return noticiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Noticia no encontrada con id=" + id));
    }

    /**
     * Actualiza una noticia existente. Suponemos que el ID ya viene en el objeto `noticia`.
     * Además permitimos cambiar el usuario si fuera necesario (o dejar el mismo).
     */
    public Noticia actualizarNoticia(Integer id, Noticia datosActualizar, String usuarioUsername) {
        Noticia existente = noticiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Noticia no encontrada con id=" + id));

        // Actualiza campos básicos
        existente.setTitulo(datosActualizar.getTitulo());
        existente.setContenido(datosActualizar.getContenido());
        existente.setIdiomaCodigo(datosActualizar.getIdiomaCodigo());

        // Si nos pasan un nuevo usuario, verificar y asignar
        if (usuarioUsername != null) {
            Usuario usuario = usuarioRepository.findByUsuario(usuarioUsername)
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id=" + usuarioUsername));
            existente.setUsuario(usuario);
        }

        return noticiaRepository.save(existente);
    }

    /**
     * Elimina una noticia por su ID. Si no existe, lanza EntityNotFoundException.
     */
    public void eliminarNoticia(Integer id) {
        boolean existe = noticiaRepository.existsById(id);
        if (!existe) {
            throw new EntityNotFoundException("Noticia no encontrada con id=" + id);
        }
        noticiaRepository.deleteById(id);
    }

}
