package com.educationalSystem.service;

import com.educationalSystem.mapper.BookMapper;
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
    private final BookMapper bookMapper;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(b -> bookMapper.convertToDTO(b, new BookDTO()))
                .toList();
    }

    public BookDTO getBookById(Long id) {
        Book book = findOrThrow(id);
        return bookMapper.convertToDTO(book, new BookDTO());
    }

    @Transactional
    public BookDTO addBook(BookDTO dto) {
        Book book = bookMapper.convertToEntity(dto, new Book());
        bookRepository.save(book);
        return bookMapper.convertToDTO(book, new BookDTO());
    }

    @Transactional
    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = findOrThrow(id);
        bookMapper.convertToEntity(dto, book);
        bookRepository.save(book);
        return bookMapper.convertToDTO(book, new BookDTO());
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