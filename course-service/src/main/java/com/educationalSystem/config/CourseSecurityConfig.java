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
public class CourseSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/actuator/**").permitAll()

                        //Courses
                        .requestMatchers(HttpMethod.GET, "/api/courses/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/courses/**")
                        .hasAnyRole("INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PUT, "/api/courses/**")
                        .hasAnyRole("INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/api/courses/**")
                        .hasRole("ADMINISTRATOR")

                        .requestMatchers(HttpMethod.GET, "/api/lessons/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/lessons/**")
                        .hasAnyRole("INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PUT, "/api/lessons/**")
                        .hasAnyRole("INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/api/lessons/**")
                        .hasRole("ADMINISTRATOR")

                        // Enrollments
                        .requestMatchers(HttpMethod.GET, "/api/enrollments/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/enrollments/**")
                        .hasAnyRole("STUDENT", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.PATCH, "/api/enrollments/**")
                        .hasAnyRole("STUDENT", "ADMINISTRATOR")

                        // Reviews
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**")
                        .hasAnyRole("STUDENT", "INSTRUCTOR", "LIBRARIAN", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.POST, "/api/reviews/**")
                        .hasAnyRole("STUDENT", "ADMINISTRATOR")

                        .requestMatchers(HttpMethod.DELETE, "/api/reviews/**")
                        .hasRole("ADMINISTRATOR")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}