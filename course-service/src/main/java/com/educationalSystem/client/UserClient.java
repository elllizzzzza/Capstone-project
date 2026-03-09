package com.educationalSystem.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.build();
    }

    public boolean userExists(Long userId) {
        System.out.println(">>> [course-service] Calling user-service for userId: " + userId);
        try {
            webClient
                    .get()
                    .uri("http://user-service/api/users/" + userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            System.err.println(">>> UserClient FAILED: " + e.getClass().getName() + " - " + e.getMessage());
            throw new RuntimeException("Cannot reach user-service", e);
        }
    }
}
