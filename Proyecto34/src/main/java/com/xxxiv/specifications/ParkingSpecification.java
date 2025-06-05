package com.xxxiv.specifications;

import com.xxxiv.dto.FiltroParkingDTO;
import com.xxxiv.model.Parking;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

public class ParkingSpecification {
    public static Specification<Parking> buildSpecification(FiltroParkingDTO filtro) {
        return (Root<Parking> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getName() != null && !filtro.getName().isBlank()) {
                predicates.add(
                        cb.like(cb.lower(root.get("name")), "%" + filtro.getName().toLowerCase() + "%")
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
