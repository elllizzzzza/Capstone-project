package com.educationalSystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportDTO {
    private String bookTitle;
    private String author;
    private long borrowCount;
}