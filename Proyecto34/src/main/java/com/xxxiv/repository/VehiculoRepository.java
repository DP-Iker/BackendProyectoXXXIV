package com.xxxiv.repository;

import com.xxxiv.model.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Integer> {

    // Puedes agregar métodos personalizados si necesitas, por ejemplo:
    // List<Vehiculo> findByEstado(Vehiculo.Estado estado);
    // List<Vehiculo> findByUbicacion(String ubicacion);

}
