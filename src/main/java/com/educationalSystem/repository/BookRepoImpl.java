package com.educationalSystem.repository;
import com.educationalSystem.entity.parts.Book;

import java.util.*;

public class BookRepoImpl implements BookRepository {
    private final Map<Long, Book> booksById = new HashMap<>();
    private final Map<String, Book> booksByTitle = new HashMap<>();

    @Override
    public Book add(Book book) {
        booksById.put(book.getBookId(), book);
        booksByTitle.put(book.getTitle(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(booksById.get(id));
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        return Optional.ofNullable(booksByTitle.get(title));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(booksById.values());
    }

    @Override
    public List<Book> findAllByAuthor(String author) {
        return booksById.values().stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    @Override
    public boolean deleteById(Long id) {
        Book removed = booksById.remove(id);
        if (removed != null) {
            booksByTitle.remove(removed.getTitle());
            return true;
        }
        return false;
    }

    @Override
    public void update(Book book) {
        booksById.put(book.getBookId(), book);
        booksByTitle.put(book.getTitle(), book);
    }
}
