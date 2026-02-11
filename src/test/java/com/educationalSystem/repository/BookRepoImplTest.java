package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SpringBootTest
@ActiveProfiles("test")
class BookRepoImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void addBook_shouldStoreBook() {
        Book book1 = new Book(1L, "Effective Java", "Joshua Bloch", "English", "Programming", true);

        bookRepository.add(book1);

        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    void repository_shouldBeEmptyInitially() {
        assertTrue(bookRepository.findAll().isEmpty());}
}