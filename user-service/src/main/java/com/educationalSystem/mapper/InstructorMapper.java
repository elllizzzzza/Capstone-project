package com.educationalSystem.mapper;

import com.educationalSystem.dto.InstructorDTO;
import com.educationalSystem.entity.user.Instructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstructorMapper implements Mapper<Instructor, InstructorDTO> {

    private final UserMapper userMapper;

    @Override
    public InstructorDTO mapToDTO(Instructor entity, InstructorDTO dto) {
        if (entity == null) return null;

        userMapper.mapToDTO(entity, dto);

        dto.setAbout(entity.getAbout());
        dto.setProfession(entity.getProfession());

        return dto;
    }

    @Override
    public Instructor mapToEntity(InstructorDTO dto, Instructor entity) {
        if (dto == null) return null;

        userMapper.mapToEntity(dto, entity);

        entity.setAbout(dto.getAbout());
        entity.setProfession(dto.getProfession());

        return entity;
    }
}
