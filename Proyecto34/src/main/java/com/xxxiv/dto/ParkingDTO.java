package com.xxxiv.dto;

import com.xxxiv.model.Parking;
import com.xxxiv.model.ParkingPolygon;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@NoArgsConstructor
public class ParkingDTO {

    private Integer id;
    private String name;
    private Integer capacity;
    private List<double[]> polygon; // cada elemento: [latitude, longitude]

    public static ParkingDTO fromEntity(Parking parking) {
        ParkingDTO dto = new ParkingDTO();
        dto.setId(parking.getId());
        dto.setName(parking.getName());
        dto.setCapacity(parking.getCapacity());

        List<double[]> coords = new ArrayList<>();
        if (parking.getPolygonPoints() != null) {
            // Recorremos los puntos ordenados por pointIndex para mantener el orden
            parking.getPolygonPoints().stream()
                    .sorted(Comparator.comparingInt(pp -> pp.getId().getPointIndex()))
                    .forEach(pp -> coords.add(new double[]{ pp.getLatitude(), pp.getLongitude() }));
        }
        dto.setPolygon(coords);

        return dto;
    }

    public static Parking toEntity(ParkingDTO dto) {
        Parking parking = new Parking();
        parking.setId(dto.getId());
        parking.setName(dto.getName());
        parking.setCapacity(dto.getCapacity());

        List<ParkingPolygon> polygons = new ArrayList<>();
        int index = 0;
        if (dto.getPolygon() != null) {
            for (double[] coord : dto.getPolygon()) {
                polygons.add(new ParkingPolygon(parking.getId(), index++, coord[0], coord[1]));
            }
        }
        parking.setPolygonPoints(polygons);

        return parking;
    }
}

