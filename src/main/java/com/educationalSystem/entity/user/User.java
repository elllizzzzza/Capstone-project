package com.educationalSystem.entity.user;

import com.educationalSystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private Role role;


    public void register(String name, String surname, String username, String email, String password, Role role){

    }

    public void login(String username, String password){

    }

    public void logout(){

    }
}
