package com.xxxiv.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImgurService {

    private final WebClient imgurWebClient;

    public String subirImagen(byte[] imagen) {
        return imgurWebClient.post()
            .uri("/image")
            .bodyValue(Map.of("image", Base64.getEncoder().encodeToString(imagen)))
            .retrieve()
            .bodyToMono(Map.class)
            .map(response -> (String) ((Map<?, ?>) response.get("data")).get("link"))
            .block();
    }
}

