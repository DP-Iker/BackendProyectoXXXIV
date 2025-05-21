package com.xxxiv.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.xxxiv.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, JpaSpecificationExecutor<Usuario> {
	
	// Crear usuario
	boolean existsByUsuario(String usuario);
    boolean existsByEmail(String email);
    
    // Login
    Optional<Usuario> findByUsuario(String usuario);
}
