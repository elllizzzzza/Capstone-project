package com.educationalSystem.entity.parts;

import com.educationalSystem.entity.user.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecord {
    private Long borrowRecordId;
    private Student student;
    private Book book;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate endDate;
    private boolean isOverdue;
}
