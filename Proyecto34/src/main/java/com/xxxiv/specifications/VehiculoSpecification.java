package com.xxxiv.specifications;

import com.xxxiv.dto.FiltroVehiculosDTO;
import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Puertas;
import com.xxxiv.model.enums.Tipo;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Optional;

public class VehiculoSpecification {
	public static Specification<Vehiculo> tieneMarca(String marca) {
		return (root, query, cb) -> marca == null ? null : cb.like(cb.lower(root.get("marca")), "%" + marca.toLowerCase() + "%");
	}

	public static Specification<Vehiculo> tieneKilometraje(Integer kilometraje) {
		return (root, query, cb) -> kilometraje == null ? null : cb.equal(root.get("kilometraje"), kilometraje);
	}

	public static Specification<Vehiculo> tieneUltimaRevision(LocalDate ultimaRevision) {
		return (root, query, cb) -> ultimaRevision == null ? null : cb.equal(root.get("ultimaRevision"), ultimaRevision);
	}

	public static Specification<Vehiculo> tieneAutonomia(Integer autonomia) {
		return (root, query, cb) -> autonomia == null ? null : cb.equal(root.get("autonomia"), autonomia);
	}

	public static Specification<Vehiculo> tieneEstado(Estado estado) {
		return (root, query, cb) -> estado == null ? null : cb.equal(root.get("estado"), estado);
	}

	public static Specification<Vehiculo> tieneLocalidad(String localidad) {
		return (root, query, cb) -> localidad == null ? null : cb.equal(root.get("localidad"), localidad);
	}

	public static Specification<Vehiculo> tieneEsAccesible(Boolean esAccesible) {
		return (root, query, cb) -> esAccesible == null ? null : cb.equal(root.get("esAccesible"), esAccesible);
	}
	
	public static Specification<Vehiculo> tienePuertas(Puertas puertas) {
		return (root, query, cb) -> puertas == null ? null : cb.equal(root.get("puertas"), puertas);
	}
	
	public static Specification<Vehiculo> tieneTipo(Tipo tipo) {
		return (root, query, cb) -> tipo == null ? null : cb.equal(root.get("tipo"), tipo);
	}

	public static Specification<Vehiculo> buildSpecification(FiltroVehiculosDTO filter) {
		return Specification
				.where(Optional.ofNullable(filter.getMarca()).map(VehiculoSpecification::tieneMarca).orElse(null))
				.and(Optional.ofNullable(filter.getKilometraje()).map(VehiculoSpecification::tieneKilometraje).orElse(null))
				.and(Optional.ofNullable(filter.getUltimaRevision()).map(VehiculoSpecification::tieneUltimaRevision).orElse(null))
				.and(Optional.ofNullable(filter.getAutonomia()).map(VehiculoSpecification::tieneAutonomia).orElse(null))
				.and(Optional.ofNullable(filter.getEstado()).map(VehiculoSpecification::tieneEstado).orElse(null))
				.and(Optional.ofNullable(filter.getLocalidad()).map(VehiculoSpecification::tieneLocalidad).orElse(null))
				.and(Optional.ofNullable(filter.getEsAccesible()).map(VehiculoSpecification::tieneEsAccesible).orElse(null))
				.and(Optional.ofNullable(filter.getPuertas()).map(VehiculoSpecification::tienePuertas).orElse(null))
				.and(Optional.ofNullable(filter.getTipo()).map(VehiculoSpecification::tieneTipo).orElse(null));
	}
}
