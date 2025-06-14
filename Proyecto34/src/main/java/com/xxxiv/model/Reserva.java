package com.xxxiv.model;

import java.time.LocalDateTime;

import com.xxxiv.model.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@Entity
@Table(name = "reserva")
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reserva_usuario"))
	private Usuario usuario;

	@OneToOne(mappedBy = "reserva", fetch = FetchType.LAZY)
	private Viaje viaje;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "vehiculo_id", foreignKey = @ForeignKey(name = "fk_reserva_vehiculo"))
	private Vehiculo vehiculo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parking_recogida_id", foreignKey = @ForeignKey(name = "fk_reserva_parking_recogida"))
	private Parking parkingRecogida;

	@CreationTimestamp
	@Column(name = "fecha_reserva", nullable = false, updatable = false)
	private LocalDateTime fechaReserva;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "ENUM('CANCELADA','CONFIRMADA','PENDIENTE')")
	private EstadoReserva estado = EstadoReserva.PENDIENTE;
}
