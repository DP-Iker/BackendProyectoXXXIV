package com.xxxiv.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@Embeddable
public class ParkingPolygonId implements Serializable {
    @Column(name = "parking_id")
    private Integer parkingId;

    @Column(name = "point_index")
    private Integer pointIndex;

    public ParkingPolygonId(Integer parkingId, Integer pointIndex) {
        this.parkingId = parkingId;
        this.pointIndex = pointIndex;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingPolygonId)) return false;
        ParkingPolygonId that = (ParkingPolygonId) o;
        return Objects.equals(parkingId, that.parkingId) &&
                Objects.equals(pointIndex, that.pointIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingId, pointIndex);
    }
}
