package com.xxxiv.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.xxxiv.dto.OpenStreetMapResponseDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LocalidadService {

    private final WebClient localidadWebClient; // Spring inyectará el único bean WebClient

    public String obtenerLocalidad(double lat, double lon) {
    	OpenStreetMapResponseDTO response = localidadWebClient.get()
    	        .uri(uriBuilder -> uriBuilder
    	            .path("/reverse")
    	            .queryParam("lat", lat)
    	            .queryParam("lon", lon)
    	            .queryParam("format", "json")
    	            .build())
    	        .retrieve()
    	        .bodyToMono(OpenStreetMapResponseDTO.class)
    	        .block();

    	    if (response == null || response.getAddress() == null) {
    	        return "localidad no encontrada";
    	    }

    	    OpenStreetMapResponseDTO.Address address = response.getAddress();

        return Optional.ofNullable(address.getCity())
                .or(() -> Optional.ofNullable(address.getTown()))
                .or(() -> Optional.ofNullable(address.getVillage()))
                .orElse("localidad no encontrada");
    }
}
