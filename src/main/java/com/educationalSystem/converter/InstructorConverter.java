package com.educationalSystem.converter;

import com.educationalSystem.dto.InstructorDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.user.Instructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstructorConverter implements Converter<Instructor, InstructorDTO> {

    private final UserConverter userConverter;

    @Override
    public InstructorDTO convertToDTO(Instructor entity, InstructorDTO dto) {
        if (entity == null) return null;

        userConverter.convertToDTO(entity, dto);

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

        userConverter.convertToEntity(dto, entity);

        entity.setAbout(dto.getAbout());
        entity.setProfession(dto.getProfession());

        return entity;
    }
}
