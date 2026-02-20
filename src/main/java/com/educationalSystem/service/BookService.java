package com.educationalSystem.service;

import com.educationalSystem.converter.BookConverter;
import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookConverter bookConverter;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(b -> bookConverter.convertToDTO(b, new BookDTO()))
                .toList();
    }

    public BookDTO getBookById(Long id) {
        Book book = findOrThrow(id);
        return bookConverter.convertToDTO(book, new BookDTO());
    }

    @Transactional
    public BookDTO addBook(BookDTO dto) {
        Book book = bookConverter.convertToEntity(dto, new Book());
        bookRepository.save(book);
        return bookConverter.convertToDTO(book, new BookDTO());
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = findOrThrow(id);
        bookConverter.convertToEntity(dto, book);
        bookRepository.save(book);
        return bookConverter.convertToDTO(book, new BookDTO());
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }

    private Book findOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }
}