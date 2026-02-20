package com.educationalSystem.mapper;

import com.educationalSystem.dto.AdministratorDTO;
import com.educationalSystem.entity.user.Administrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministratorMapper implements Converter<Administrator, AdministratorDTO> {

    private final UserMapper userMapper;

    @Override
    public AdministratorDTO convertToDTO(Administrator entity, AdministratorDTO dto) {
        if (entity == null) {
            return null;
        }

        userMapper.convertToDTO(entity, dto);

        return dto;
    }

    @Override
    public Administrator convertToEntity(AdministratorDTO dto, Administrator entity) {
        if (dto == null) {
            return null;
        }

        userMapper.convertToEntity(dto, entity);

        return entity;
    }
}
