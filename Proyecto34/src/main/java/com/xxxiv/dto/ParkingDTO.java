package com.xxxiv.dto;

import com.xxxiv.model.Coordinate;
import com.xxxiv.model.Parking;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ParkingDTO {

    private Integer id;
    private String name;
    private List<double[]> polygon; // cada elemento: [latitude, longitude]

    /**
     * Construye un DTO a partir de la entidad Parking.
     * Convierte List<Coordinate> → List<double[]> para exponerlo en la API.
     */
    public static ParkingDTO fromEntity(Parking parking) {
        ParkingDTO dto = new ParkingDTO();
        dto.setId(parking.getId());
        dto.setName(parking.getName());

        List<double[]> coords = new ArrayList<>();
        if (parking.getCods() != null) {
            for (Coordinate c : parking.getCods()) {
                coords.add(new double[]{ c.getLat(), c.getLng() });
            }
        }
        dto.setPolygon(coords);

        return dto;
    }

    /**
     * Construye una entidad Parking a partir del DTO.
     * Convierte List<double[]> → List<Coordinate> y las asigna a parking.setCods(...).
     */
    public static Parking toEntity(ParkingDTO dto) {
        Parking parking = new Parking();
        parking.setId(dto.getId());
        parking.setName(dto.getName());

        List<Coordinate> coordinates = new ArrayList<>();
        if (dto.getPolygon() != null) {
            for (double[] coord : dto.getPolygon()) {
                // El campo 'time' de Coordinate lo dejamos en null,
                // puedes adaptarlo si incorporas un timestamp en el DTO.
                coordinates.add(new Coordinate(coord[0], coord[1]));
            }
        }
        parking.setCods(coordinates);

        return parking;
    }
}
