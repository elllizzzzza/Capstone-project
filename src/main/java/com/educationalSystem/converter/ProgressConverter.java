package com.educationalSystem.converter;

import com.educationalSystem.dto.ProgressDTO;
import com.educationalSystem.entity.parts.Lesson;
import com.educationalSystem.entity.parts.Progress;
import org.springframework.stereotype.Component;

@Component
public class ProgressConverter implements Converter<Progress, ProgressDTO> {

    @Override
    public ProgressDTO convertToDTO(Progress entity, ProgressDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setCompletionRate(entity.getCompletionRate());

        dto.setCompletedLessonIds(entity.getCompletedLessons() != null
                ? entity.getCompletedLessons().stream().map(Lesson::getLessonId).toList()
                : null);

        return dto;
    }

    @Override
    public Progress convertToEntity(ProgressDTO dto, Progress entity) {
        if (dto == null) return null;

        entity.setCompletionRate(dto.getCompletionRate());

        return entity;
    }
}
