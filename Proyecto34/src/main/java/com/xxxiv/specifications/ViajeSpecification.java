package com.xxxiv.specifications;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.xxxiv.dto.FiltroViajesDTO;
import com.xxxiv.model.Viaje;

public class ViajeSpecification {

    public static Specification<Viaje> porUsuarioId(Integer usuarioId) {
        return (root, query, cb) ->
            usuarioId == null ? null : cb.equal(root.get("usuario").get("id"), usuarioId);
    }
    
    public static Specification<Viaje> porVehiculoId(Integer vehiculoId) {
        return (root, query, cb) ->
        	vehiculoId == null ? null : cb.equal(root.get("vehiculo").get("id"), vehiculoId);
    }

    public static Specification<Viaje> fechaInicio(LocalDateTime fechaInicio) {
        return (root, query, cb) ->
        fechaInicio == null ? null : cb.greaterThanOrEqualTo(root.get("fechaInicio"), fechaInicio);
    }

    public static Specification<Viaje> fechaFin(LocalDateTime fechaFin) {
        return (root, query, cb) ->
        fechaFin == null ? null : cb.greaterThanOrEqualTo(root.get("fechaFin"), fechaFin);
    }
    
    public static Specification<Viaje> porKmRecorridos(Integer kmRecorridos) {
        return (root, query, cb) ->
        	kmRecorridos == null ? null : cb.equal(root.get("kmRecorridos"), kmRecorridos);
    }
    
    public static Specification<Viaje> porPrecio(Double precio) {
        return (root, query, cb) ->
        precio == null ? null : cb.equal(root.get("precio"), precio);
    }

    public static Specification<Viaje> buildSpecification(FiltroViajesDTO filter) {
        return Specification.where(
                Optional.ofNullable(filter.getUsuarioId())
                        .map(ViajeSpecification::porUsuarioId)
                        .orElse(null))
        	.and(Optional.ofNullable(filter.getVehiculoId())
                        .map(ViajeSpecification::porVehiculoId)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getFechaInicio())
                        .map(ViajeSpecification::fechaInicio)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getFechaFin())
                        .map(ViajeSpecification::fechaFin)
                        .orElse(null))
        	.and(Optional.ofNullable(filter.getKmRecorridos())
        				.map(ViajeSpecification::porKmRecorridos)
        				.orElse(null))
        	.and(Optional.ofNullable(filter.getPrecio())
        				.map(ViajeSpecification::porPrecio)
        				.orElse(null));
    }
}