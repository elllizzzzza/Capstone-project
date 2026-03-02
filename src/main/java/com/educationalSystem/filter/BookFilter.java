package com.educationalSystem.filter;

import lombok.Data;

import java.util.List;

@Data
public class BookFilter {

    private List<String> genre;

    private List<String> language;

    private String author;

    private String title;

    private Integer availableCopiesMin;
    private Integer availableCopiesMax;

    private Integer totalCopiesMin;
    private Integer totalCopiesMax;
}