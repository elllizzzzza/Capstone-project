package com.educationalSystem.dto;

import lombok.Data;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private String language;
    private String genre;
}
