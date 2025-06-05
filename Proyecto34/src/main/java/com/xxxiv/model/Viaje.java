package com.xxxiv.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.xxxiv.util.CoordinateListConverter;
import jakarta.persistence.*;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_viaje_usuario"))
	private Usuario usuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehiculo_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_viaje_vehiculo"))
	private Vehiculo vehiculo;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "reserva_id", referencedColumnName = "id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_viaje_reserva"))
	private Reserva reserva;

	@Column(name = "fecha_inicio", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", columnDefinition = "DATETIME")
    private LocalDateTime fechaFin;

	@Column(name = "km_recorridos")
	private Integer kmRecorridos;

	@Column(name = "precio")
	private Double precio;

	@Convert(converter = CoordinateListConverter.class)
	@Column(columnDefinition = "JSON", nullable = false)
	private List<Coordinate> cods = new ArrayList<>();
}
