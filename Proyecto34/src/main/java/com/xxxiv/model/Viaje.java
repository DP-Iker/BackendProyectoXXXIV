package com.xxxiv.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="viaje")
public class Viaje {
	
	@Id
	@GeneratedValue(strategy = 	GenerationType.IDENTITY)
	private Integer id;
	
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_viaje_usuario"))
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "vehiculo_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_viaje_vehiculo"))
    private Vehiculo vehiculo;
	
  
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

   
    @Column(name = "km_recorridos")
    private Integer kmRecorridos;
    
    
	

}
