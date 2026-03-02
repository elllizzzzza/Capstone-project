package com.educationalSystem.mapper;

import com.educationalSystem.dto.StudentDTO;
import com.educationalSystem.entity.user.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper implements Mapper<Student, StudentDTO> {

    private final UserMapper userMapper;

    @Override
    public StudentDTO mapToDTO(Student entity, StudentDTO dto) {
        if (entity == null) return null;

        userMapper.mapToDTO(entity, dto);

        dto.setUniversity(entity.getUniversity());
        dto.setUniId(entity.getUniId());
        dto.setCardDetails(entity.getCardDetails());

        return dto;
    }

    @Override
    public Student mapToEntity(StudentDTO dto, Student entity) {
        if (dto == null) return null;

        userMapper.mapToEntity(dto, entity);

        entity.setUniversity(dto.getUniversity());
        entity.setUniId(dto.getUniId());
        entity.setCardDetails(dto.getCardDetails());

        return entity;
    }
}
