package com.xxxiv.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	@Column(nullable = false, unique = true, length = 25)
	private String usuario;
	
	@NotNull
	@JsonIgnore
	@Column(nullable = false, length = 60)
	private String contrasenya;
	
	@Email
	@NotNull
	@Column(nullable = false, unique = true, length = 40)
	private String email;
	
	@Column(length = 100)
    private String foto;
	
	@Column(nullable = false)
	private boolean esAdministrador;
	
	@Column(nullable = false)
	private boolean estaBloqueado;
	
	@Lob
	@Column(columnDefinition = "MEDIUMTEXT")
	private String motivoBloqueo;
	
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
}