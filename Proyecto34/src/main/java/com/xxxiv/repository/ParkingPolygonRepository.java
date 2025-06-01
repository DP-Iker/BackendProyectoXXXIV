package com.xxxiv.repository;

import com.xxxiv.model.Parking;
import com.xxxiv.model.ParkingPolygon;
import com.xxxiv.model.ParkingPolygonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkingPolygonRepository extends JpaRepository<ParkingPolygon, ParkingPolygonId> {
    List<ParkingPolygon> findByIdParkingId(Integer parkingId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ParkingPolygon pp WHERE pp.parkingId = :parkingId")
    void deleteAllByParkingId(@Param("parkingId") Integer parkingId);
}
