package com.educationalSystem.config;

import com.educationalSystem.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class RoomSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/actuator/**").permitAll()

                        // Rooms
                        .requestMatchers(HttpMethod.GET, "/api/rooms/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/rooms/**")
                        .hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PUT, "/api/rooms/**")
                        .hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/api/rooms/**")
                        .hasRole("ADMINISTRATOR")

                        // Bookings
                        .requestMatchers(HttpMethod.GET, "/api/bookings")
                        .hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/bookings/student/**")
                        .hasAnyRole("STUDENT", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/bookings/instructor/**")
                        .hasAnyRole("INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/bookings/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PATCH, "/api/bookings/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMINISTRATOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}