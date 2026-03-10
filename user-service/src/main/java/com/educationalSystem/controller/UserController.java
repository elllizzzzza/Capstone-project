package com.educationalSystem.controller;

import com.educationalSystem.dto.UserDTO;
import com.educationalSystem.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;
    private final Environment environment;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        System.out.println(">>> Request on port: " + environment.getProperty("server.port"));
        return ResponseEntity.ok(userManagementService.getUserById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMyProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername(); // always safe
        System.out.println("Authenticated username: " + username);
        UserDTO user = userManagementService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}