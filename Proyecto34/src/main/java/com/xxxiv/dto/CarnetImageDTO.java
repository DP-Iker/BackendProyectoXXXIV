package com.xxxiv.dto;

import com.xxxiv.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class CarnetImageDTO {
    private String usuario;
    private MultipartFile imagen;
}
