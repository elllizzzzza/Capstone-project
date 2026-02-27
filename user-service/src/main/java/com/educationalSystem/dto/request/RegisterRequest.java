package com.educationalSystem.dto.request;

import com.educationalSystem.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    private String surname;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "Password must contain at least one digit, one lowercase, and one uppercase letter")
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    // Student-specific
    @Size(max = 100, message = "University name cannot exceed 100 characters")
    private String university;

    @Size(max = 20, message = "University ID cannot exceed 20 characters")
    private String uniId;

    // Instructor-specific
    @Size(max = 500, message = "About section cannot exceed 500 characters")
    private String about;

    @Size(max = 100, message = "Profession cannot exceed 100 characters")
    private String profession;
}