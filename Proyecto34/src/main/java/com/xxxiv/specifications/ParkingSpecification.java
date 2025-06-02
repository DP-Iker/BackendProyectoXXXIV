package com.xxxiv.specifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.model.Parking;

import jakarta.persistence.criteria.Predicate;

public class ParkingSpecification {

    public static Specification<Parking> buildSpecification(FiltroParkingDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getNombre() != null && !filtro.getNombre().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filtro.getNombre().toLowerCase() + "%"));
            }

            if (filtro.getCapacidadMinima() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capacity"), filtro.getCapacidadMinima()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
