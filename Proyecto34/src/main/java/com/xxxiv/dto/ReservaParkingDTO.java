package com.xxxiv.dto;

import com.xxxiv.model.Parking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaParkingDTO {
    private Integer id;
    private String name;

    public ReservaParkingDTO(Parking p) {
        this.id = p.getId();
        this.name = p.getName();
    }
}