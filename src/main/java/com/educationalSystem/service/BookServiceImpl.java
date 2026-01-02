package com.educationalSystem.service;

import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book createBook(Book book) {
        return repository.add(book);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Book> getBookByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        return repository.findAllByAuthor(author);
    }

    @Override
    public void updateBook(Book book) {
        repository.update(book);
    }

    @Override
    public boolean deleteBook(Long id) {
        return repository.deleteById(id);
    }
}
