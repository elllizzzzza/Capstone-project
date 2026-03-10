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
public class LibrarySecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/actuator/**").permitAll()

                        // Books
                        .requestMatchers(HttpMethod.GET, "/api/books/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/books/**")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PUT, "/api/books/**")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/api/books/**")
                        .hasRole("ADMINISTRATOR")

                        // Borrows
                        .requestMatchers(HttpMethod.GET, "/api/borrows/student/**")
                        .hasAnyRole("STUDENT", "LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/borrows/overdue")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/borrows/reports/**")
                        .hasAnyRole("LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/borrows/**")
                        .hasAnyRole("STUDENT", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PATCH, "/api/borrows/**")
                        .hasAnyRole("STUDENT", "LIBRARIAN", "ADMINISTRATOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}