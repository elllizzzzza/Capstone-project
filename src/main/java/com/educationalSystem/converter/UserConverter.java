package com.educationalSystem.converter;

import com.educationalSystem.dto.UserDTO;
import com.educationalSystem.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convertToDTO(User entity, UserDTO dto) {
        if (entity == null) {
            return null;
        }

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());

        dto.setRole(entity.getRole());

        return dto;
    }

    @Override
    public User convertToEntity(UserDTO dto, User entity) {
        if (dto == null) {
            return null;
        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());

        return entity;
    }
}
