package com.educationalSystem.repository;

import com.educationalSystem.entity.user.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
