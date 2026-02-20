package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByStudent_Id(Long studentId);

    @Query("SELECT b FROM BorrowRecord b WHERE b.endDate IS NULL AND b.dueDate < :today")
    List<BorrowRecord> findOverdue(LocalDate today);

    Optional<BorrowRecord> findByStudent_IdAndBook_IdAndEndDateIsNull(Long studentId, Long bookId);

    @Query("SELECT b.book.title, b.book.author, COUNT(b) as cnt FROM BorrowRecord b GROUP BY b.book.id ORDER BY cnt DESC")
    List<Object[]> findMostBorrowedBooks();
}
