package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourseCourseId(Long courseId);
    boolean existsByStudentIdAndCourseCourseId(Long studentId, Long courseId);
}
