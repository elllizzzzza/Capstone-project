package com.educationalSystem.service;

import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.mapper.BookMapper;
import com.educationalSystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Unit Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setLanguage("English");
        book.setGenre("Programming");
        book.setTotalCopies(5);
        book.setAvailableCopies(5);

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setTitle("Clean Code");
        bookDTO.setAuthor("Robert Martin");
        bookDTO.setLanguage("English");
        bookDTO.setGenre("Programming");
        bookDTO.setTotalCopies(5);
        bookDTO.setAvailableCopies(5);
    }

    @Nested
    @DisplayName("getBookById()")
    class GetBookByIdTests {

        @Test
        @DisplayName("Should return book DTO when found")
        void getBookById_Found() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(bookMapper.convertToDTO(book, new BookDTO())).thenReturn(bookDTO);

            BookDTO result = bookService.getBookById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("Clean Code");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when book not found")
        void getBookById_NotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookService.getBookById(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Book not found: 99");
        }
    }

    @Nested
    @DisplayName("addBook()")
    class AddBookTests {

        @Test
        @DisplayName("Should add book successfully")
        void addBook_Success() {
            when(bookMapper.convertToEntity(any(), any())).thenReturn(book);
            when(bookMapper.convertToDTO(any(), any())).thenReturn(bookDTO);

            BookDTO result = bookService.addBook(bookDTO);

            assertThat(result).isNotNull();
            verify(bookRepository).save(book);
        }
    }

    @Nested
    @DisplayName("updateBook()")
    class UpdateBookTests {

        @Test
        @DisplayName("Should update book when found")
        void updateBook_Success() {
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(bookMapper.convertToEntity(any(), any())).thenReturn(book);
            when(bookMapper.convertToDTO(any(), any())).thenReturn(bookDTO);

            BookDTO result = bookService.updateBook(1L, bookDTO);

            assertThat(result).isNotNull();
            verify(bookRepository).save(book);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when updating non-existent book")
        void updateBook_NotFound() {
            when(bookRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> bookService.updateBook(99L, bookDTO))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteBook()")
    class DeleteBookTests {

        @Test
        @DisplayName("Should delete book when found")
        void deleteBook_Success() {
            when(bookRepository.existsById(1L)).thenReturn(true);

            bookService.deleteBook(1L);

            verify(bookRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when deleting non-existent book")
        void deleteBook_NotFound() {
            when(bookRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> bookService.deleteBook(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Book not found: 99");
        }
    }

    @Nested
    @DisplayName("searchBooks()")
    class SearchBooksTests {

        @Test
        @DisplayName("Should return matching books")
        void searchBooks_ReturnsResults() {
            when(bookRepository.findByTitleContainingIgnoreCase("clean"))
                    .thenReturn(List.of(book));
            when(bookMapper.convertToDTO(any(), any())).thenReturn(bookDTO);

            List<BookDTO> results = bookService.searchBooks("clean");

            assertThat(results).hasSize(1);
        }

        @Test
        @DisplayName("Should return empty list when no match")
        void searchBooks_NoMatch() {
            when(bookRepository.findByTitleContainingIgnoreCase("zzz"))
                    .thenReturn(List.of());

            List<BookDTO> results = bookService.searchBooks("zzz");

            assertThat(results).isEmpty();
        }
    }
}