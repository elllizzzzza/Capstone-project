package com.educationalSystem.controller;

import com.educationalSystem.dto.UserDTO;
import com.educationalSystem.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;
    private final Environment environment;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        System.out.println(">>> Request received on port: " +
                environment.getProperty("server.port"));
        return ResponseEntity.ok(userManagementService.getUserById(id));
    }
}