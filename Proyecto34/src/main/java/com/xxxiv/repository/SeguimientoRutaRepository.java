package com.xxxiv.repository;

import com.xxxiv.model.SeguimientoRuta;
import com.xxxiv.model.SeguimientoRutaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeguimientoRutaRepository extends JpaRepository<SeguimientoRuta, SeguimientoRutaId> {
    @Query("SELECT sr FROM SeguimientoRuta sr WHERE sr.id.viajeId = :viajeId ORDER BY sr.id.puntoIndex ASC")
    List<SeguimientoRuta> findByViajeIdOrderByIdPuntoIndexAsc(@Param("viajeId") Integer viajeId);

}
