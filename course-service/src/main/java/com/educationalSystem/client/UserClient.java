package com.educationalSystem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final WebClient.Builder webClientBuilder;
    private final DiscoveryClient discoveryClient;

    private String getUserServiceUrl() {
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");
        if (instances.isEmpty()) {
            throw new RuntimeException("user-service is not available");
        }
        return instances.get(0).getUri().toString();
    }

    public boolean userExists(Long userId) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri(getUserServiceUrl() + "/api/users/" + userId)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        }
    }
}