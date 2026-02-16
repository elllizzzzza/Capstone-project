package com.educationalSystem.converter;

import com.educationalSystem.dto.CourseDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Lesson;
import com.educationalSystem.entity.parts.Review;
import org.springframework.stereotype.Component;

@Component
public class CourseConverter implements Converter<Course, CourseDTO> {

    @Override
    public CourseDTO convertToDTO(Course entity, CourseDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getCourseId());
        dto.setCourseName(entity.getCourseName());
        dto.setCategory(entity.getCategory());
        dto.setType(entity.getType());
        dto.setLevel(entity.getLevel());
        dto.setInstructorId(entity.getInstructor() != null ? entity.getInstructor().getId() : null);

        dto.setLessonsIds(entity.getLessons() != null
                ? entity.getLessons().stream().map(Lesson::getLessonId).toList()
                : null);

        dto.setReviewsIds(entity.getReviews() != null
                ? entity.getReviews().stream().map(Review::getId).toList()
                : null);

        return dto;
    }

    @Override
    public Course convertToEntity(CourseDTO dto, Course entity) {
        if (dto == null) return null;

        entity.setCourseName(dto.getCourseName());
        entity.setCategory(dto.getCategory());
        entity.setType(dto.getType());
        entity.setLevel(dto.getLevel());

        return entity;
    }
}
