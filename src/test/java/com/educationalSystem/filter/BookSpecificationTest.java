package com.educationalSystem.filter;

import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookSpecificationTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.saveAll(List.of(
                book("The Hobbit",        "Tolkien", "English", "Fantasy", 10, 5),
                book("Lord of the Rings", "Tolkien", "English", "Fantasy", 20, 0),
                book("Dune",              "Herbert", "English", "Sci-Fi",  15, 8),
                book("Solaris",           "Lem",     "Polish",  "Sci-Fi",   5, 5)
        ));
    }

    @Test
    void genreIn_returnsOnlyMatchingGenre() {
        Specification<Book> spec = BookSpecification.genreIn(List.of("Fantasy"));
        List<Book> result = bookRepository.findAll(spec);
        assertThat(result).hasSize(2)
                .allMatch(b -> b.getGenre().equals("Fantasy"));
    }

    @Test
    void genreIn_multipleValues_returnsAll() {
        Specification<Book> spec = BookSpecification.genreIn(List.of("Fantasy", "Sci-Fi"));
        assertThat(bookRepository.findAll(spec)).hasSize(4);
    }

    @Test
    void genreIn_emptyList_returnsAll() {
        Specification<Book> spec = BookSpecification.genreIn(List.of());
        assertThat(bookRepository.findAll(spec)).hasSize(4);
    }

    @Test
    void languageIn_singleValue() {
        Specification<Book> spec = BookSpecification.languageIn(List.of("Polish"));
        assertThat(bookRepository.findAll(spec)).hasSize(1)
                .allMatch(b -> b.getLanguage().equals("Polish"));
    }

    @Test
    void authorContains_caseInsensitive() {
        Specification<Book> spec = BookSpecification.authorContains("TOLKIEN");
        assertThat(bookRepository.findAll(spec)).hasSize(2);
    }

    @Test
    void titleContains_partialMatch() {
        Specification<Book> spec = BookSpecification.titleContains("the");
        assertThat(bookRepository.findAll(spec)).hasSize(2);
    }

    @Test
    void availableCopiesMin_greaterThanOrEqual() {
        Specification<Book> spec = BookSpecification.availableCopiesMin(5);
        List<Book> result = bookRepository.findAll(spec);
        assertThat(result).allMatch(b -> b.getAvailableCopies() >= 5);
        assertThat(result).hasSize(3);
    }

    @Test
    void availableCopiesMax_lessThanOrEqual() {
        Specification<Book> spec = BookSpecification.availableCopiesMax(0);
        List<Book> result = bookRepository.findAll(spec);
        assertThat(result).hasSize(1)
                .allMatch(b -> b.getAvailableCopies() == 0);
    }

    @Test
    void combinedFilter_genreAndAvailability() {
        BookFilter filter = new BookFilter();
        filter.setGenre(List.of("Sci-Fi"));
        filter.setAvailableCopiesMin(1);

        List<Book> result = bookRepository.findAll(BookSpecification.fromFilter(filter));
        assertThat(result).hasSize(2)
                .allMatch(b -> b.getGenre().equals("Sci-Fi") && b.getAvailableCopies() >= 1);
    }

    @Test
    void emptyFilter_returnsAll() {
        assertThat(bookRepository.findAll(BookSpecification.fromFilter(new BookFilter()))).hasSize(4);
    }

    private Book book(String title, String author, String lang, String genre, int total, int available) {
        Book b = new Book();
        b.setTitle(title); b.setAuthor(author); b.setLanguage(lang);
        b.setGenre(genre); b.setTotalCopies(total); b.setAvailableCopies(available);
        return b;
    }
}