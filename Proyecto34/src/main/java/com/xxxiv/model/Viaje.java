package com.xxxiv.model;

import java.time.LocalDate;
import java.util.List;

import com.xxxiv.util.CoordinateListConverter;
import jakarta.persistence.*;
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


    @Convert(converter = CoordinateListConverter.class)
    @Column(columnDefinition = "JSON", nullable = false)
    private List<Coordinate> cods;

}
