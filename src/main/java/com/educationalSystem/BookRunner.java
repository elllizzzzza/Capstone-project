package com.educationalSystem;

import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class BookRunner implements CommandLineRunner {

    private final BookService bookService;

    public BookRunner(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) {
        Book book1 = new Book(1L, "Effective Java", "Joshua Bloch", "English", "Programming", true);
        Book book2 = new Book(2L, "Clean Code", "Robert C. Martin", "English", "Programming", true);

        bookService.createBook(book1);
        bookService.createBook(book2);

        System.out.println("All Books:");
        bookService.getAllBooks().forEach(System.out::println);

        System.out.println("Find by Title 'Clean Code':");
        System.out.println(bookService.getBookByTitle("Clean Code").orElse(null));

        book1.setAvailable(false);
        bookService.updateBook(book1);
        System.out.println("Updated Book 1: " + bookService.getBookById(1L).orElse(null));

        bookService.deleteBook(2L);
        System.out.println("Books after deletion of 2nd book:");
        bookService.getAllBooks().forEach(System.out::println);

        System.out.println("All book categories:");
        bookService.printCategories();
    }
}

