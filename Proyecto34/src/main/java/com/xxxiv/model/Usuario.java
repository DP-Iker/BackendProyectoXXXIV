package com.xxxiv.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@Column(nullable = false, unique = true)
	private String usuario;
	
	@NotNull
	@Column(nullable = false)
	private String contrasenya;
	
	@Email
	@NotNull
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private boolean estaBloqueado;
	
	@Lob
	@Column(columnDefinition = "MEDIUMTEXT")
	private String motivoBloqueo;
	
	@NotNull
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
}