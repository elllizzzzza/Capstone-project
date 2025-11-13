package com.educationalSystem.service;

import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private BookRepository repository;
    private BookServiceImpl service;


    private Book book1;
    private Book book2;
    private List<Book> books;

    @BeforeEach
    void setUp() {
        repository = mock(BookRepository.class);
        service = new BookServiceImpl(repository);
        book1 = new Book(1L, "Book A", "Author A", "eng", "g", true);
        book2 = new Book(2L, "Book B", "Author B", "eng", "g", true);
        books = List.of(book1, book2);
    }

    @Test
    void createBook() {
        when(repository.add(book1)).thenReturn(book1);
        Book result = service.createBook(book1);

        assertEquals(book1, result);
        verify(repository, times(1)).add(book1);
    }

    @Test
    void getBookById() {
        when(repository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> result = service.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void getBookById_NotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = service.getBookById(99L);

        assertFalse(result.isPresent());
        verify(repository).findById(99L);
    }

    @Test
    void getBookByTitle() {
        when(repository.findByTitle("Book A")).thenReturn(Optional.of(book1));

        Optional<Book> result = service.getBookByTitle("Book A");

        assertTrue(result.isPresent());
        assertEquals(book1, result.get());
        verify(repository).findByTitle("Book A");
    }

    @Test
    void getAllBooks() {
        when(repository.findAll()).thenReturn(books);

        List<Book> result = service.getAllBooks();

        assertEquals(2, result.size());
        assertEquals(books, result);
        verify(repository).findAll();
    }

    @Test
    void getBooksByAuthor() {
        when(repository.findAllByAuthor("Author A")).thenReturn(List.of(book1));

        List<Book> result = service.getBooksByAuthor("Author A");

        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(b -> b.getAuthor().equals("Author A")));
        verify(repository).findAllByAuthor("Author A");
    }

    @Test
    void updateBook() {
        Book book3 = new Book(1L, "Book new", "Author A", "eng", "g", true);
        service.updateBook(book3);

        verify(repository, times(1)).update(book3);
    }

    @Test
    void deleteBook() {
        when(repository.deleteById(1L)).thenReturn(true);

        boolean result = service.deleteBook(1L);

        assertTrue(result);
        verify(repository).deleteById(1L);
    }
}