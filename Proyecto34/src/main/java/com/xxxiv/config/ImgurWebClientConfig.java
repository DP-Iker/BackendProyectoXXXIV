package com.xxxiv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ImgurWebClientConfig {

    @Value("${imgur.client-id}")
    private String clientId;

    @Bean
    WebClient imgurWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.imgur.com/3")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Client-ID " + clientId)
                .build();
    }
}
