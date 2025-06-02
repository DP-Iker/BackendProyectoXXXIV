package com.xxxiv.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "viaje")
public class Viaje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_viaje_usuario"))
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "vehiculo_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_viaje_vehiculo"))
	private Vehiculo vehiculo;

	@Column(name = "fecha_inicio", nullable = false)
	private LocalDate fechaInicio;

	@Column(name = "fecha_fin")
	private LocalDate fechaFin;

	@Column(name = "km_recorridos")
	private Integer kmRecorridos;

	@Column(name = "latitud")
	private Double latitud;

	@Column(name = "longitud")
	private Double longitud;
	
	@Column(name = "precio")
	private Double precio;

	// Método para calcular el precio del viaje
	public Double calcularPrecio() {
		if (kmRecorridos == null || vehiculo == null || vehiculo.getTipo() == null) {
			return 0.0;
		}
		double precioPorKm;
		switch (vehiculo.getTipo()) {
		case TURISMO:
			precioPorKm = 0.5;
			break;
		case SUV:
			precioPorKm = 0.7;
			break;
		case BIPLAZA:
			precioPorKm = 0.4;
			break;
		case MONOVOLUMEN:
			precioPorKm = 0.6;
			break;
		default:
			precioPorKm = 0.5;
		}
		return kmRecorridos * precioPorKm;
	}

	public void finalizarViaje(LocalDate fechaFin, Integer kmRecorridos) {
		this.setFechaFin(fechaFin);
		this.setKmRecorridos(kmRecorridos);
		this.setPrecio(calcularPrecio());
	}

}
