package com.educationalSystem.dto;

import com.educationalSystem.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private Role role;
}
