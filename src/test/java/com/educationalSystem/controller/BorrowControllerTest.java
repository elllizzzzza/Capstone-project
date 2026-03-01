package com.educationalSystem.controller;

import com.educationalSystem.dto.BorrowRecordDTO;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.service.BorrowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowController.class)
@DisplayName("BorrowController Integration Tests")
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BorrowService borrowService;

    private BorrowRecordDTO sampleDTO() {
        BorrowRecordDTO dto = new BorrowRecordDTO();
        dto.setBorrowRecordId(1L);
        dto.setStudentId(1L);
        dto.setBookId(1L);
        dto.setBorrowDate(LocalDate.now());
        dto.setDueDate(LocalDate.now().plusDays(14));
        return dto;
    }

    @Nested
    @DisplayName("GET /api/borrows/student/{studentId}")
    class GetHistoryByStudentTests {

        @Test
        @DisplayName("200 - Returns borrow history for student")
        void getHistoryByStudent_200() throws Exception {
            when(borrowService.getBorrowHistoryByStudent(1L)).thenReturn(List.of(sampleDTO()));

            mockMvc.perform(get("/api/borrows/student/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].studentId").value(1))
                    .andExpect(jsonPath("$[0].bookId").value(1));
        }

        @Test
        @DisplayName("200 - Returns empty list when no history")
        void getHistoryByStudent_Empty() throws Exception {
            when(borrowService.getBorrowHistoryByStudent(1L)).thenReturn(List.of());

            mockMvc.perform(get("/api/borrows/student/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("GET /api/borrows/overdue")
    class GetOverdueTests {

        @Test
        @DisplayName("200 - Returns overdue borrow records")
        void getOverdueRecords_200() throws Exception {
            BorrowRecordDTO overdueDTO = sampleDTO();
            overdueDTO.setOverdue(true);
            when(borrowService.getOverdueRecords()).thenReturn(List.of(overdueDTO));

            mockMvc.perform(get("/api/borrows/overdue"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].overdue").value(true));
        }
    }

    @Nested
    @DisplayName("POST /api/borrows/student/{studentId}/book/{bookId}")
    class BorrowBookTests {

        @Test
        @DisplayName("201 - Successfully borrows book")
        void borrowBook_201() throws Exception {
            when(borrowService.borrowBook(1L, 1L)).thenReturn(sampleDTO());

            mockMvc.perform(post("/api/borrows/student/1/book/1"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.studentId").value(1))
                    .andExpect(jsonPath("$.bookId").value(1));
        }

        @Test
        @DisplayName("404 - Student or Book not found")
        void borrowBook_404() throws Exception {
            when(borrowService.borrowBook(99L, 1L))
                    .thenThrow(new ResourceNotFoundException("Student not found"));

            mockMvc.perform(post("/api/borrows/student/99/book/1"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.type").value("resource-not-found"))
                    .andExpect(jsonPath("$.detail").value("Student not found"));
        }

        @Test
        @DisplayName("400 - No available copies")
        void borrowBook_400_NoCopies() throws Exception {
            when(borrowService.borrowBook(1L, 1L))
                    .thenThrow(new BusinessException("No available copies of this book"));

            mockMvc.perform(post("/api/borrows/student/1/book/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("business-rule-violation"))
                    .andExpect(jsonPath("$.detail").value("No available copies of this book"));
        }

        @Test
        @DisplayName("400 - Book already borrowed by student")
        void borrowBook_400_AlreadyBorrowed() throws Exception {
            when(borrowService.borrowBook(1L, 1L))
                    .thenThrow(new BusinessException("You already have this book borrowed"));

            mockMvc.perform(post("/api/borrows/student/1/book/1"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("You already have this book borrowed"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/borrows/student/{studentId}/book/{bookId}/return")
    class ReturnBookTests {

        @Test
        @DisplayName("200 - Successfully returns book")
        void returnBook_200() throws Exception {
            BorrowRecordDTO returned = sampleDTO();
            returned.setEndDate(LocalDate.now());
            when(borrowService.returnBook(1L, 1L)).thenReturn(returned);

            mockMvc.perform(patch("/api/borrows/student/1/book/1/return"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.endDate").isNotEmpty());
        }

        @Test
        @DisplayName("400 - No active borrow record")
        void returnBook_400_NoActiveRecord() throws Exception {
            when(borrowService.returnBook(1L, 1L))
                    .thenThrow(new BusinessException("No active borrow record found"));

            mockMvc.perform(patch("/api/borrows/student/1/book/1/return"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PATCH /api/borrows/{borrowRecordId}/mark-returned")
    class MarkReturnedTests {

        @Test
        @DisplayName("200 - Successfully marks record as returned")
        void markReturned_200() throws Exception {
            when(borrowService.markReturned(1L)).thenReturn(sampleDTO());

            mockMvc.perform(patch("/api/borrows/1/mark-returned"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("404 - Borrow record not found")
        void markReturned_404() throws Exception {
            when(borrowService.markReturned(99L))
                    .thenThrow(new ResourceNotFoundException("Borrow record not found"));

            mockMvc.perform(patch("/api/borrows/99/mark-returned"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("400 - Book already returned")
        void markReturned_400_AlreadyReturned() throws Exception {
            when(borrowService.markReturned(1L))
                    .thenThrow(new BusinessException("This book has already been returned"));

            mockMvc.perform(patch("/api/borrows/1/mark-returned"))
                    .andExpect(status().isBadRequest());
        }
    }
}