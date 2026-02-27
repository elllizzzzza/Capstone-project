package com.educationalSystem.mapper;

import com.educationalSystem.dto.ReviewDTO;
import com.educationalSystem.entity.parts.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper implements Mapper<Review, ReviewDTO> {

    @Override
    public ReviewDTO mapToDTO(Review entity, ReviewDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setStudentId(entity.getStudentId());
        dto.setCourseId(entity.getCourse() != null ? entity.getCourse().getCourseId() : null);
        dto.setComment(entity.getComment());
        dto.setRating(entity.getRating());
        dto.setDate(entity.getDate());

        return dto;
    }

    @Override
    public Review mapToEntity(ReviewDTO dto, Review entity) {
        if (dto == null) return null;

        entity.setStudentId(dto.getStudentId());
        entity.setComment(dto.getComment());
        entity.setRating(dto.getRating());
        entity.setDate(dto.getDate());

        return entity;
    }
}
