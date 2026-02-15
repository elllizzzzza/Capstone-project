package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
