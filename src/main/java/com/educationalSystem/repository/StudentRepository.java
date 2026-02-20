package com.educationalSystem.repository;

import com.educationalSystem.entity.user.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
