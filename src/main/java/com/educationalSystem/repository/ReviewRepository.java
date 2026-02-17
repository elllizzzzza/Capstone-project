package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourse_CourseId(Long courseId);
    boolean existsByStudent_IdAndCourse_CourseId(Long studentId, Long courseId);
}
