package com.xxxiv.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarnetImageDTO {
    private String usuario;
    private MultipartFile imagen;
}
