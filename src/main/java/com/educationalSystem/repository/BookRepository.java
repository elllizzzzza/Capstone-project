package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    List<Book> findByGenre(String genre);
    List<Object> findByTitleContainingIgnoreCase(String keyword);
}
