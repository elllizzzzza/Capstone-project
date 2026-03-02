package com.educationalSystem.mapper;

import com.educationalSystem.dto.EnrollmentDTO;
import com.educationalSystem.entity.parts.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentMapper implements Mapper<Enrollment, EnrollmentDTO> {

    @Override
    public EnrollmentDTO mapToDTO(Enrollment entity, EnrollmentDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudentId());
        dto.setCourseId(entity.getCourse() != null ? entity.getCourse().getCourseId() : null);
        dto.setEnrollmentDate(entity.getEnrollmentDate());
        dto.setProgressId(entity.getProgress() != null ? entity.getProgress().getId() : null);

        return dto;
    }

    @Override
    public Enrollment mapToEntity(EnrollmentDTO dto, Enrollment entity) {
        if (dto == null) return null;
        entity.setId(dto.getId());
        entity.setStudentId(dto.getStudentId());
        entity.setEnrollmentDate(dto.getEnrollmentDate());

        return entity;
    }
}
