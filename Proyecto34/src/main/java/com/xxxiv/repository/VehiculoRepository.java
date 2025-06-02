package com.xxxiv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.xxxiv.model.Vehiculo;
import com.xxxiv.model.enums.Estado;
import com.xxxiv.model.enums.Tipo;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer>, JpaSpecificationExecutor<Vehiculo> {
    List<Vehiculo> findByEstado(Estado estado);
    
    List<Vehiculo> findByEstadoAndTipo(Estado estado, Tipo tipo);
    
    @Query("SELECT DISTINCT v.localidad FROM Vehiculo v WHERE v.estado = :estado")
    List<String> buscarLocalidadesDisponibles(@Param("estado") Estado estado);

    @Query("SELECT DISTINCT v.marca FROM Vehiculo v WHERE v.estado = :estado")
    List<String> buscarMarcasDisponibles(@Param("estado") Estado estado);
}
