package com.educationalSystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReviewDTO {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String comment;
    private int rating;
    private LocalDate date;
}
