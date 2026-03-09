package com.educationalSystem.client;

import com.educationalSystem.exception.ResourceNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder.build();
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "userExistsFallback")
    @Retry(name = "user-service")
    public boolean userExists(Long userId) {
        log.info("Calling user-service for userId: {}", userId);
        try {
            webClient
                    .get()
                    .uri("http://user-service/api/users/" + userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            throw new ResourceNotFoundException("User not found: " + userId);
        } catch (WebClientResponseException e) {
            log.warn("user-service returned error: {}", e.getStatusCode());
            throw e;
        } catch (Exception e) {
            log.warn("Failed to reach user-service: {}", e.getMessage());
            throw new RuntimeException("Cannot reach user-service", e);
        }
    }

    private boolean userExistsFallback(Long userId, Throwable t) {
        log.error("Circuit breaker fallback for userId: {}. Reason: {}", userId, t.getMessage());
        throw new RuntimeException(
                "User service is currently unavailable. Please try again later.", t);
    }
}