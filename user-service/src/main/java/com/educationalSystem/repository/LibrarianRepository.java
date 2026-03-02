package com.educationalSystem.repository;

import com.educationalSystem.entity.user.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
}
