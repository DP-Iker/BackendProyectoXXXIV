package com.xxxiv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.xxxiv.model.Parking;

public interface ParkingRepository extends JpaRepository<Parking, Integer>, JpaSpecificationExecutor<Parking> {
}
