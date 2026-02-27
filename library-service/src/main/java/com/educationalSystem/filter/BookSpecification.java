package com.educationalSystem.filter;

import com.educationalSystem.entity.parts.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class BookSpecification {

    private BookSpecification() {}

    public static Specification<Book> genreIn(List<String> genres) {
        return (root, query, cb) ->
                CollectionUtils.isEmpty(genres) ? null
                        : root.get("genre").in(genres);
    }

    public static Specification<Book> languageIn(List<String> languages) {
        return (root, query, cb) ->
                CollectionUtils.isEmpty(languages) ? null
                        : root.get("language").in(languages);
    }

    public static Specification<Book> authorContains(String author) {
        return (root, query, cb) ->
                author == null ? null
                        : cb.like(cb.lower(root.get("author")),
                        "%" + author.toLowerCase() + "%");
    }

    public static Specification<Book> titleContains(String title) {
        return (root, query, cb) ->
                title == null ? null
                        : cb.like(cb.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> availableCopiesMin(Integer min) {
        return (root, query, cb) ->
                min == null ? null
                        : cb.greaterThanOrEqualTo(root.get("availableCopies"), min);
    }

    public static Specification<Book> availableCopiesMax(Integer max) {
        return (root, query, cb) ->
                max == null ? null
                        : cb.lessThanOrEqualTo(root.get("availableCopies"), max);
    }

    public static Specification<Book> totalCopiesMin(Integer min) {
        return (root, query, cb) ->
                min == null ? null
                        : cb.greaterThanOrEqualTo(root.get("totalCopies"), min);
    }

    public static Specification<Book> totalCopiesMax(Integer max) {
        return (root, query, cb) ->
                max == null ? null
                        : cb.lessThanOrEqualTo(root.get("totalCopies"), max);
    }

    public static Specification<Book> fromFilter(BookFilter filter) {
        return Specification.allOf(
                genreIn(filter.getGenre()),
                languageIn(filter.getLanguage()),
                authorContains(filter.getAuthor()),
                titleContains(filter.getTitle()),
                availableCopiesMin(filter.getAvailableCopiesMin()),
                availableCopiesMax(filter.getAvailableCopiesMax()),
                totalCopiesMin(filter.getTotalCopiesMin()),
                totalCopiesMax(filter.getTotalCopiesMax())
        );
    }
}