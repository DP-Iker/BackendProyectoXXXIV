package com.xxxiv.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta del directorio raíz del proyecto
        String absolutePath = Paths.get(uploadDir).toAbsolutePath().getParent().toString().replace("\\", "/");

        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations("file:///" + absolutePath + "/");
    }
}