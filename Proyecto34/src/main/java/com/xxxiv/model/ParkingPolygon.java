package com.xxxiv.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "parking")
@Entity
@Table(name = "parking_polygon")
public class ParkingPolygon {

    @EmbeddedId
    private ParkingPolygonId id;

    @Column(name = "parking_id", nullable = false, insertable = false, updatable = false)
    private Integer parkingId;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    public ParkingPolygon(Integer parkingId, Integer pointIndex, Double latitude, Double longitude) {
        this.id = new ParkingPolygonId(parkingId, pointIndex);
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
