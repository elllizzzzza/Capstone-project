package com.educationalSystem.service;

import com.educationalSystem.dto.response.PagedResponse;
import com.educationalSystem.filter.BookFilter;
import com.educationalSystem.filter.BookSpecification;
import com.educationalSystem.mapper.BookMapper;
import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public PagedResponse<BookDTO> getAllBooks(BookFilter filter, Pageable pageable) {
        return PagedResponse.from(
                bookRepository.findAll(BookSpecification.fromFilter(filter), pageable)
                        .map(b -> bookMapper.convertToDTO(b, new BookDTO()))
        );
    }

    public BookDTO getBookById(Long id) {
        return bookMapper.convertToDTO(findOrThrow(id), new BookDTO());
    }

    public List<BookDTO> searchBooks(String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(b -> bookMapper.convertToDTO((Book) b, new BookDTO()))
                .toList();
    }

    public List<BookDTO> filterByGenre(String genre) {
        return bookRepository.findByGenre(genre).stream()
                .map(b -> bookMapper.convertToDTO(b, new BookDTO()))
                .toList();
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