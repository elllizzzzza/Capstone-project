package com.educationalSystem.repository;


import com.educationalSystem.entity.parts.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book add(Book book);
    Optional<Book> findById(Long id);
    Optional<Book> findByTitle(String title);
    List<Book> findAll();
    List<Book> findAllByAuthor(String author);
    boolean deleteById(Long id);
    void update(Book book);
}
