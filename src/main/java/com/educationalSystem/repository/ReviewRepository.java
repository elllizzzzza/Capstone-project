package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
