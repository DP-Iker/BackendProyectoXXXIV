package com.xxxiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "caracteristica")
public class Caracteristica {

    @Id
    @Column(name = "vehiculo_id")
    private Integer vehiculoId;

    @Convert(converter = PuertasConverter.class)
    @Column(nullable = false)
    private Puertas puertas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipo tipo;

    @Column(nullable = false, length = 45)
    private String color;

    @Column(name = "es_accesible", nullable = false)
    private Boolean esAccesible = false;

    // Getters y Setters

    public enum Puertas {
        _3("3"),
        _5("5");
    	
        private final String valor;
        
        Puertas(String valor) {
        	this.valor = valor; 
        }
        
        public String getValor() {
        	return valor;
        }
        
        public static Puertas fromValue(String valor) {
            for (Puertas p : values()) {
                if (p.getValor().equals(valor)) {
                    return p;
                }
            }
            throw new IllegalArgumentException("No enum constant for value: " + valor);
        }
        @Override 
        public String toString() { 
        	return valor;
        }
    }

    public enum Tipo {
        Turismo, SUV, Biplaza, Monovolumen
    }
}
