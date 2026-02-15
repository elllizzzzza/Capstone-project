package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
