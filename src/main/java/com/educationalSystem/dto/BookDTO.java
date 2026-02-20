package com.educationalSystem.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BookDTO {

    private Long id;

    @NotBlank(message = "Book title is required")
    @Size(min = 1, max = 300, message = "Title must be between 1 and 300 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 2, max = 200, message = "Author must be between 2 and 200 characters")
    private String author;

    @NotBlank(message = "Language is required")
    @Size(min = 2, max = 50, message = "Language must be between 2 and 50 characters")
    private String language;

    @NotBlank(message = "Genre is required")
    @Size(min = 2, max = 100, message = "Genre must be between 2 and 100 characters")
    private String genre;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "Total copies must be at least 1")
    @Max(value = 1000, message = "Total copies cannot exceed 1000")
    private Integer totalCopies;

    @Min(value = 0, message = "Available copies cannot be negative")
    private Integer availableCopies;
}