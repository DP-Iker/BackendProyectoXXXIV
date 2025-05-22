package com.xxxiv.repository;

import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Localidad;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer>, JpaSpecificationExecutor<Vehiculo> {
    List<Vehiculo> findByEstado(Estado estado);
    
    @Query("SELECT DISTINCT v.localidad FROM Vehiculo v WHERE v.estado = :estado")
    List<Localidad> buscarLocalidadesDisponibles(@Param("estado") Estado estado);

}
