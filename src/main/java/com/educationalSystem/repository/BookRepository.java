package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByGenre(String genre);
    List<Object> findByTitleContainingIgnoreCase(String keyword);
}
