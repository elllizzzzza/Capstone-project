package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
