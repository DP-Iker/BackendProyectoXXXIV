package com.xxxiv.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "polygon")
@Entity
@Table(name = "parking")
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parking_id")
    private List<ParkingPolygon> polygonPoints = new ArrayList<>();


    public Parking(String name, Integer capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    /**
     * Añade un vértice al polígono con el índice especificado.
     */
    public void addPolygonPoint(int pointIndex, double latitude, double longitude) {
        if (this.id == null) {
            throw new IllegalStateException("Parking ID must be set before adding polygon points");
        }
        ParkingPolygon p = new ParkingPolygon(this.id, pointIndex, latitude, longitude);
        this.polygonPoints.add(p);
    }

    /**
     * Elimina todos los vértices del polígono.
     */
    public void clearPolygonPoints() {
        this.polygonPoints.clear();
    }
}
