package com.xxxiv.specifications;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

import com.xxxiv.dto.FiltroReservasDTO;
import com.xxxiv.model.Reserva;
import com.xxxiv.model.enums.EstadoReserva;

public class ReservaSpecification {

    public static Specification<Reserva> porUsuarioId(Integer usuarioId) {
        return (root, query, cb) ->
            usuarioId == null ? null : cb.equal(root.get("usuario").get("id"), usuarioId);
    }
    
    public static Specification<Reserva> porVehiculoId(Integer vehiculoId) {
        return (root, query, cb) ->
        	vehiculoId == null ? null : cb.equal(root.get("vehiculo").get("id"), vehiculoId);
    }

    public static Specification<Reserva> porEstado(EstadoReserva estado) {
        return (root, query, cb) ->
            estado == null ? null : cb.equal(root.get("estado"), estado);
    }

    public static Specification<Reserva> fechaReserva(LocalDateTime fechaReserva) {
        return (root, query, cb) ->
        fechaReserva == null ? null : cb.greaterThanOrEqualTo(root.get("fechaReserva"), fechaReserva);
    }

    public static Specification<Reserva> buildSpecification(FiltroReservasDTO filter) {
        return Specification.where(
                Optional.ofNullable(filter.getUsuarioId())
                        .map(ReservaSpecification::porUsuarioId)
                        .orElse(null))
        	.and(Optional.ofNullable(filter.getVehiculoId())
                        .map(ReservaSpecification::porVehiculoId)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getEstado())
                        .map(ReservaSpecification::porEstado)
                        .orElse(null))
            .and(Optional.ofNullable(filter.getFechaReserva())
                        .map(ReservaSpecification::fechaReserva)
                        .orElse(null));
    }
}