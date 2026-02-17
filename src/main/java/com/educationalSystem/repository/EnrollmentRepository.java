package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent_Id(Long studentId);
    List<Enrollment> findByCourse_CourseId(Long courseId);
    Optional<Enrollment> findByStudent_IdAndCourse_CourseId(Long studentId, Long courseId);
    boolean existsByStudent_IdAndCourse_CourseId(Long studentId, Long courseId);
}
