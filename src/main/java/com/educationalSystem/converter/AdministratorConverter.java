package com.educationalSystem.converter;

import com.educationalSystem.dto.AdministratorDTO;
import com.educationalSystem.entity.user.Administrator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdministratorConverter implements Converter<Administrator, AdministratorDTO> {

    private final UserConverter userConverter;

    @Override
    public AdministratorDTO convertToDTO(Administrator entity, AdministratorDTO dto) {
        if (entity == null) {
            return null;
        }

        userConverter.convertToDTO(entity, dto);

        return dto;
    }

    @Override
    public Administrator convertToEntity(AdministratorDTO dto, Administrator entity) {
        if (dto == null) {
            return null;
        }

        userConverter.convertToEntity(dto, entity);

        return entity;
    }
}
