package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseCourseId(Long courseId);
    Optional<Enrollment> findByStudentIdAndCourseCourseId(Long studentId, Long courseId);
    boolean existsByStudentIdAndCourseCourseId(Long studentId, Long courseId);
}
