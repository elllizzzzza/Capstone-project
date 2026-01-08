package com.educationalSystem;

import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.service.BookService;
import com.educationalSystem.service.BookServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        BookService bookService = context.getBean(BookServiceImpl.class);

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
    }
}

