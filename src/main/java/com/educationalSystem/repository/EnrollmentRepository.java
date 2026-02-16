package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}
