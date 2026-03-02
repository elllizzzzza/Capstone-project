package com.educationalSystem.mapper;

import com.educationalSystem.dto.AdministratorDTO;
import com.educationalSystem.entity.user.Administrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministratorMapper implements Mapper<Administrator, AdministratorDTO> {

    private final UserMapper userMapper;

    @Override
    public AdministratorDTO mapToDTO(Administrator entity, AdministratorDTO dto) {
        if (entity == null) {
            return null;
        }

        userMapper.mapToDTO(entity, dto);

        return dto;
    }

    @Override
    public Administrator mapToEntity(AdministratorDTO dto, Administrator entity) {
        if (dto == null) {
            return null;
        }

        userMapper.mapToEntity(dto, entity);

        return entity;
    }
}
