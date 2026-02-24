package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByInstructorId(Long instructorId);
    List<Course> findByCourseNameContainingIgnoreCase(String keyword);
}
