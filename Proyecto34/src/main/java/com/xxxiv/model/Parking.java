package com.xxxiv.model;

import com.xxxiv.model.Coordinate;
import com.xxxiv.util.CoordinateListConverter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "parking")
public class Parking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Convert(converter = CoordinateListConverter.class)
    @Column(columnDefinition = "JSON", nullable = false)
    private List<Coordinate> cods;
}
