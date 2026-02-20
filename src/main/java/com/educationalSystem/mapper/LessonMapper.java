package com.educationalSystem.mapper;

import com.educationalSystem.dto.LessonDTO;
import com.educationalSystem.entity.parts.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapper implements Converter<Lesson, LessonDTO> {

    @Override
    public LessonDTO convertToDTO(Lesson entity, LessonDTO dto) {
        if (entity == null) return null;

        dto.setLessonId(entity.getLessonId());
        dto.setTitle(entity.getTitle());
        dto.setDurationMinutes(entity.getDurationMinutes());
        dto.setCourseId(entity.getCourse() != null ? entity.getCourse().getCourseId() : null);

        return dto;
    }

    @Override
    public Lesson convertToEntity(LessonDTO dto, Lesson entity) {
        if (dto == null) return null;

        entity.setTitle(dto.getTitle());
        entity.setDurationMinutes(dto.getDurationMinutes());

        return entity;
    }
}
