package com.educationalSystem.service;

import com.educationalSystem.entity.parts.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book createBook(Book book);
    Optional<Book> getBookById(Long id);
    Optional<Book> getBookByTitle(String title);
    List<Book> getAllBooks();
    List<Book> getBooksByAuthor(String author);
    void updateBook(Book book);
    boolean deleteBook(Long id);
}
