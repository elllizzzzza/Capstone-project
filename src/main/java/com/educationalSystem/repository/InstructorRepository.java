package com.educationalSystem.repository;

import com.educationalSystem.entity.user.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
