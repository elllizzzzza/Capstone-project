package com.educationalSystem.mapper;

import com.educationalSystem.dto.InstructorDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.user.Instructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstructorMapper implements Converter<Instructor, InstructorDTO> {

    private final UserMapper userMapper;

    @Override
    public InstructorDTO convertToDTO(Instructor entity, InstructorDTO dto) {
        if (entity == null) return null;

        userMapper.convertToDTO(entity, dto);

        dto.setAbout(entity.getAbout());
        dto.setProfession(entity.getProfession());

        if (entity.getMyCourses() != null) {
            dto.setMyCoursesIds(entity.getMyCourses()
                    .stream()
                    .map(Course::getCourseId)
                    .toList());
        }

        return dto;
    }

    @Override
    public Instructor convertToEntity(InstructorDTO dto, Instructor entity) {
        if (dto == null) return null;

        userMapper.convertToEntity(dto, entity);

        entity.setAbout(dto.getAbout());
        entity.setProfession(dto.getProfession());

        return entity;
    }
}
