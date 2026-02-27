package com.educationalSystem.service;

import com.educationalSystem.client.UserClient;
import com.educationalSystem.mapper.BorrowRecordMapper;
import com.educationalSystem.dto.BorrowRecordDTO;
import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.entity.parts.BorrowRecord;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.*;
import com.educationalSystem.dto.response.ReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final UserClient userClient;
    private final BookRepository bookRepository;
    private final BorrowRecordMapper borrowRecordMApper;

    private static final int LOAN_DAYS = 14;

    public List<BorrowRecordDTO> getBorrowHistoryByStudent(Long studentId) {
        return borrowRecordRepository.findByStudentId(studentId).stream()
                .map(b -> borrowRecordMApper.mapToDTO(b, new BorrowRecordDTO()))
                .toList();
    }

    public List<BorrowRecordDTO> getOverdueRecords() {
        return borrowRecordRepository.findOverdue(LocalDate.now()).stream()
                .map(b -> borrowRecordMApper.mapToDTO(b, new BorrowRecordDTO()))
                .toList();
    }

    @Transactional
    public BorrowRecordDTO borrowBook(Long studentId, Long bookId) {
        if (!userClient.userExists(studentId)) {
            throw new ResourceNotFoundException("Student not found");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessException("No available copies of this book");
        }

        borrowRecordRepository.findByStudentIdAndBookIdAndEndDateIsNull(studentId, bookId)
                .ifPresent(r -> { throw new BusinessException("You already have this book borrowed"); });

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord record = new BorrowRecord();
        record.setStudentId(studentId);
        record.setBook(book);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(LOAN_DAYS));
        borrowRecordRepository.save(record);

        return borrowRecordMApper.mapToDTO(record, new BorrowRecordDTO());
    }

    @Transactional
    public BorrowRecordDTO returnBook(Long studentId, Long bookId) {
        BorrowRecord record = borrowRecordRepository
                .findByStudentIdAndBookIdAndEndDateIsNull(studentId, bookId)
                .orElseThrow(() -> new BusinessException("No active borrow record found"));

        record.setEndDate(LocalDate.now());
        borrowRecordRepository.save(record);

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRecordMApper.mapToDTO(record, new BorrowRecordDTO());
    }

    @Transactional
    public BorrowRecordDTO markReturned(Long borrowRecordId) {
        BorrowRecord record = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        if (record.getEndDate() != null) {
            throw new BusinessException("This book has already been returned");
        }

        record.setEndDate(LocalDate.now());
        borrowRecordRepository.save(record);

        Book book = record.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRecordMApper.mapToDTO(record, new BorrowRecordDTO());
    }

    public List<ReportDTO> getMostBorrowedBooks() {
        return borrowRecordRepository.findMostBorrowedBooks().stream()
                .map(row -> new ReportDTO((String) row[0], (String) row[1], (long) row[2]))
                .toList();
    }
}