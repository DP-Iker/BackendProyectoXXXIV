package com.xxxiv.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ParkingDTO {
    private Integer id;
    private String name;
    private Integer capacity;
    private Double longitud;
    private Double latitud;
}
