package com.educationalSystem.mapper;

import com.educationalSystem.dto.CourseDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Lesson;
import com.educationalSystem.entity.parts.Review;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper implements Mapper<Course, CourseDTO> {

    @Override
    public CourseDTO mapToDTO(Course entity, CourseDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getCourseId());
        dto.setCourseName(entity.getCourseName());
        dto.setCategory(entity.getCategory());
        dto.setType(entity.getType());
        dto.setLevel(entity.getLevel());
        dto.setInstructorId(entity.getInstructorId());

        dto.setLessonIds(entity.getLessons() != null
                ? entity.getLessons().stream().map(Lesson::getLessonId).toList()
                : null);

        dto.setReviewIds(entity.getReviews() != null
                ? entity.getReviews().stream().map(Review::getId).toList()
                : null);

        return dto;
    }

    @Override
    public Course mapToEntity(CourseDTO dto, Course entity) {
        if (dto == null) return null;

        entity.setCourseName(dto.getCourseName());
        entity.setCategory(dto.getCategory());
        entity.setType(dto.getType());
        entity.setLevel(dto.getLevel());
        entity.setInstructorId(dto.getInstructorId());

        return entity;
    }
}
