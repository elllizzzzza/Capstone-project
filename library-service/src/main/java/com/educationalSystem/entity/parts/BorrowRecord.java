package com.educationalSystem.entity.parts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    private Long studentId;

    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate endDate;

    @Transient
    public boolean isOverdue() {
        return endDate == null && LocalDate.now().isAfter(dueDate);
    }
}
