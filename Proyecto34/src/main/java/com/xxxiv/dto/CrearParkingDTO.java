package com.xxxiv.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class CrearParkingDTO {
    @NotBlank
    private String name;

    @NotNull
    @Min(1)
    private Integer capacity;
}
