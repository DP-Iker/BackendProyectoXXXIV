package com.xxxiv.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="vehiculo")
public class Vehiculo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String marca;
	private String modelo;
	private byte[] imagen;
	private int kilometraje;
	private LocalDate ultimaRevision;
	private int autonomia;
	private Estado estado;
	private String ubicacion;
	
	private enum Estado {  // Enum definido dentro de la clase de entidad
        DISPONIBLE,
        EN_USO,
        RESERVADO,
        EN_MANTENIMIENTO
    }
}