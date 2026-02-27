package com.educationalSystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRecordDTO {
    private Long borrowRecordId;
    private Long studentId;
    private Long bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate endDate;
    private boolean overdue;
}
