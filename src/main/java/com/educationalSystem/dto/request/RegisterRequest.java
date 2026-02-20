// dto/request/RegisterRequest.java
package com.educationalSystem.dto.request;

import com.educationalSystem.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private Role role;

    // Student-specific
    private String university;
    private String uniId;

    // Instructor-specific
    private String about;
    private String profession;
}