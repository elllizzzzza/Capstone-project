package com.educationalSystem.service;

import com.educationalSystem.dto.request.LoginRequest;
import com.educationalSystem.dto.request.RegisterRequest;
import com.educationalSystem.dto.response.AuthResponse;
import com.educationalSystem.entity.user.*;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException("Username already taken: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already registered: " + request.getEmail());
        }

        User user = buildUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        //todo Replace "mock-jwt-token" with jwtService.generateToken(user) when JWT is wired
        return new AuthResponse("mock-jwt-token", user.getId(), user.getUsername(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid credentials");
        }

        return new AuthResponse("mock-jwt-token", user.getId(), user.getUsername(), user.getRole());
    }

    private User buildUser(RegisterRequest req) {
        return switch (req.getRole()) {
            case STUDENT -> {
                Student s = new Student();
                applyCommon(s, req);
                s.setUniversity(req.getUniversity());
                s.setUniId(req.getUniId());
                yield s;
            }
            case INSTRUCTOR -> {
                Instructor i = new Instructor();
                applyCommon(i, req);
                i.setAbout(req.getAbout());
                i.setProfession(req.getProfession());
                yield i;
            }
            case LIBRARIAN -> {
                Librarian l = new Librarian();
                applyCommon(l, req);
                yield l;
            }
            case ADMINISTRATOR -> {
                Administrator a = new Administrator();
                applyCommon(a, req);
                yield a;
            }
        };
    }

    private void applyCommon(User user, RegisterRequest req) {
        user.setName(req.getName());
        user.setSurname(req.getSurname());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setRole(req.getRole());
    }
}