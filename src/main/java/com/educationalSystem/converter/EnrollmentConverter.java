package com.educationalSystem.converter;

import com.educationalSystem.dto.EnrollmentDTO;
import com.educationalSystem.entity.parts.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentConverter implements Converter<Enrollment, EnrollmentDTO> {

    @Override
    public EnrollmentDTO convertToDTO(Enrollment entity, EnrollmentDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getId() : null);
        dto.setCourseId(entity.getCourse() != null ? entity.getCourse().getCourseId() : null);
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setProgressId(entity.getProgress() != null ? entity.getProgress().getId() : null);

        return dto;
    }

    @Override
    public Enrollment convertToEntity(EnrollmentDTO dto, Enrollment entity) {
        if (dto == null) return null;

        entity.setEnrollmentDate(dto.getEnrollmentDate());

        return entity;
    }
}
