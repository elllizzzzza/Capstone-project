package com.educationalSystem.service;

import com.educationalSystem.config.BookConfig;
import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final BookConfig bookConfig;

    public BookServiceImpl(BookRepository repository, BookConfig bookConfig) {
        this.repository = repository;
        this.bookConfig = bookConfig;
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

    public void printCategories() {
        for (String cat : bookConfig.getCategories()) {
            System.out.println(cat);
        }
    }
}
