package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseCourseId(Long courseId);

    int countByCourseCourseId(Long courseId);
}
