package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}
