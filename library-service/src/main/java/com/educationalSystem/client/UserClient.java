package com.educationalSystem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${services.user-service.url}")
    private String userServiceUrl;

    public boolean userExists(Long userId) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri(userServiceUrl + "/api/users/" + userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        }
    }
}