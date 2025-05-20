package com.xxxiv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xxxiv.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
	boolean existsByUsuario(String usuario);
    boolean existsByEmail(String email);
    
    Optional<Usuario> findByUsuario(String usuario);
}
