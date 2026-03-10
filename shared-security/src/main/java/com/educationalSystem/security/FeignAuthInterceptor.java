package com.educationalSystem.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignAuthInterceptor implements RequestInterceptor {

    private final JwtService jwtService;

    private static final String SERVICE_TOKEN_USERNAME = "internal-service";
    private static final String SERVICE_ROLE = "SERVICE";

    @Override
    public void apply(RequestTemplate template) {
        String serviceToken = jwtService.generateToken(SERVICE_TOKEN_USERNAME, SERVICE_ROLE);
        template.header("Authorization", "Bearer " + serviceToken);
    }
}