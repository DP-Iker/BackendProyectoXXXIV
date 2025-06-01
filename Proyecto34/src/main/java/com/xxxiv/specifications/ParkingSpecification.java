package com.xxxiv.specifications;

import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.model.Parking;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public class ParkingSpecification {

    public static Specification<Parking> nombreContiene(String nombre) {
        return (root, query, cb) ->
                nombre == null
                        ? null
                        : cb.like(cb.lower(root.get("name")), "%" + nombre.toLowerCase() + "%");
    }

    public static Specification<Parking> capacidadMinima(Integer min) {
        return (root, query, cb) ->
                min == null
                        ? null
                        : cb.greaterThanOrEqualTo(root.get("capacity"), min);
    }

    public static Specification<Parking> capacidadMaxima(Integer max) {
        return (root, query, cb) ->
                max == null
                        ? null
                        : cb.lessThanOrEqualTo(root.get("capacity"), max);
    }

    public static Specification<Parking> buildSpecification(FiltroParkingDTO filter) {
        return Specification.where(
                        Optional.ofNullable(filter.getName())
                                .map(ParkingSpecification::nombreContiene)
                                .orElse(null))
                .and(Optional.ofNullable(filter.getCapacidadMinima())
                        .map(ParkingSpecification::capacidadMinima)
                        .orElse(null))
                .and(Optional.ofNullable(filter.getCapacidadMaxima())
                        .map(ParkingSpecification::capacidadMaxima)
                        .orElse(null));
    }
}
