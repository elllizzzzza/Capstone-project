package com.educationalSystem.converter;

import com.educationalSystem.dto.ReviewDTO;
import com.educationalSystem.entity.parts.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewConverter implements Converter<Review, ReviewDTO> {

    @Override
    public ReviewDTO convertToDTO(Review entity, ReviewDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudent() != null ? entity.getStudent().getId() : null);
        dto.setCourseId(entity.getCourse() != null ? entity.getCourse().getCourseId() : null);
        dto.setComment(entity.getComment());
        dto.setRating(entity.getRating());
        dto.setDate(entity.getDate());

        return dto;
    }

    @Override
    public Review convertToEntity(ReviewDTO dto, Review entity) {
        if (dto == null) return null;

        entity.setComment(dto.getComment());
        entity.setRating(dto.getRating());
        entity.setDate(dto.getDate());

        return entity;
    }
}
