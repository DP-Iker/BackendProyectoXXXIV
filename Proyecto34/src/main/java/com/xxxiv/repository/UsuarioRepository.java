package com.xxxiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xxxiv.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {}
