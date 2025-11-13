package com.educationalSystem.entity.parts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    private Long bookId;
    private String title;
    private String author;
    private String language;
    private String genre;
    private Boolean available;
}
