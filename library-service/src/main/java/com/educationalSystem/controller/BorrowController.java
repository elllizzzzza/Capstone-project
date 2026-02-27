package com.educationalSystem.controller;

import com.educationalSystem.dto.BorrowRecordDTO;
import com.educationalSystem.dto.response.ReportDTO;
import com.educationalSystem.service.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<BorrowRecordDTO>> getHistoryByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(borrowService.getBorrowHistoryByStudent(studentId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowRecordDTO>> getOverdueRecords() {
        return ResponseEntity.ok(borrowService.getOverdueRecords());
    }

    @GetMapping("/reports/most-borrowed")
    public ResponseEntity<List<ReportDTO>> getMostBorrowedBooks() {
        return ResponseEntity.ok(borrowService.getMostBorrowedBooks());
    }

    @PostMapping("/student/{studentId}/book/{bookId}")
    public ResponseEntity<BorrowRecordDTO> borrowBook(@PathVariable Long studentId,
                                                      @PathVariable Long bookId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowService.borrowBook(studentId, bookId));
    }

    @PatchMapping("/student/{studentId}/book/{bookId}/return")
    public ResponseEntity<BorrowRecordDTO> returnBook(@PathVariable Long studentId,
                                                      @PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.returnBook(studentId, bookId));
    }

    @PatchMapping("/{borrowRecordId}/mark-returned")
    public ResponseEntity<BorrowRecordDTO> markReturned(@PathVariable Long borrowRecordId) {
        return ResponseEntity.ok(borrowService.markReturned(borrowRecordId));
    }
}