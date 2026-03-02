package com.educationalSystem.controller;

import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.dto.response.PagedResponse;
import com.educationalSystem.exception.GlobalExceptionHandler;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.filter.BookFilter;
import com.educationalSystem.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import({GlobalExceptionHandler.class})
@DisplayName("BookController Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    private BookDTO validBookDTO;
    private PagedResponse<BookDTO> pagedResponse;

    @BeforeEach
    void setUp() {
        validBookDTO = new BookDTO();
        validBookDTO.setId(1L);
        validBookDTO.setTitle("Clean Code");
        validBookDTO.setAuthor("Robert Martin");
        validBookDTO.setLanguage("English");
        validBookDTO.setGenre("Programming");
        validBookDTO.setTotalCopies(5);
        validBookDTO.setAvailableCopies(5);

        Page<BookDTO> page = new PageImpl<>(List.of(validBookDTO));
        pagedResponse = PagedResponse.from(page);
    }

    @Nested
    @DisplayName("GET /api/books")
    class GetAllBooksTests {

        @Test
        @DisplayName("200 - Returns paged list of books")
        void getAllBooks_200() throws Exception {
            when(bookService.getAllBooks(any(BookFilter.class), any(Pageable.class)))
                    .thenReturn(pagedResponse);

            mockMvc.perform(get("/api/books"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title").value("Clean Code"))
                    .andExpect(jsonPath("$.content[0].author").value("Robert Martin"))
                    .andExpect(jsonPath("$.totalItems").value(1))
                    .andExpect(jsonPath("$.currentPage").value(1));
        }

        @Test
        @DisplayName("200 - Returns empty paged response")
        void getAllBooks_EmptyList() throws Exception {
            Page<BookDTO> emptyPage = new PageImpl<>(List.of());
            when(bookService.getAllBooks(any(BookFilter.class), any(Pageable.class)))
                    .thenReturn(PagedResponse.from(emptyPage));

            mockMvc.perform(get("/api/books"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.totalItems").value(0));
        }

        @Test
        @DisplayName("200 - Pagination params are passed through")
        void getAllBooks_withPaginationParams() throws Exception {
            when(bookService.getAllBooks(any(BookFilter.class), any(Pageable.class)))
                    .thenReturn(pagedResponse);

            mockMvc.perform(get("/api/books")
                            .param("page", "0")
                            .param("size", "5")
                            .param("sort", "title,asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].title").value("Clean Code"));
        }

        @Test
        @DisplayName("200 - Filter by genre")
        void getAllBooks_filterByGenre() throws Exception {
            when(bookService.getAllBooks(any(BookFilter.class), any(Pageable.class)))
                    .thenReturn(pagedResponse);

            mockMvc.perform(get("/api/books").param("genre", "Programming"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].genre").value("Programming"));
        }

        @Test
        @DisplayName("200 - Filter by author partial match")
        void getAllBooks_filterByAuthor() throws Exception {
            when(bookService.getAllBooks(any(BookFilter.class), any(Pageable.class)))
                    .thenReturn(pagedResponse);

            mockMvc.perform(get("/api/books").param("author", "martin"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalItems").value(1));
        }

        @Test
        @DisplayName("200 - Filter by availableCopies range")
        void getAllBooks_filterByAvailableCopiesRange() throws Exception {
            when(bookService.getAllBooks(any(BookFilter.class), any(Pageable.class)))
                    .thenReturn(pagedResponse);

            mockMvc.perform(get("/api/books")
                            .param("availableCopiesMin", "1")
                            .param("availableCopiesMax", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].availableCopies").value(5));
        }
    }

    @Nested
    @DisplayName("GET /api/books/{id}")
    class GetBookByIdTests {

        @Test
        @DisplayName("200 - Returns book by ID")
        void getBookById_200() throws Exception {
            when(bookService.getBookById(1L)).thenReturn(validBookDTO);

            mockMvc.perform(get("/api/books/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.title").value("Clean Code"));
        }

        @Test
        @DisplayName("404 - Book not found")
        void getBookById_404() throws Exception {
            when(bookService.getBookById(99L))
                    .thenThrow(new ResourceNotFoundException("Book not found: 99"));

            mockMvc.perform(get("/api/books/99"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.type").value("resource-not-found"))
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    @Nested
    @DisplayName("POST /api/books")
    class AddBookTests {

        @Test
        @DisplayName("201 - Creates book with valid data")
        void addBook_201() throws Exception {
            when(bookService.addBook(any())).thenReturn(validBookDTO);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.title").value("Clean Code"));
        }

        @Test
        @DisplayName("400 - Validation fails: blank title")
        void addBook_400_BlankTitle() throws Exception {
            validBookDTO.setTitle("");

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.type").value("validation-error"))
                    .andExpect(jsonPath("$.errors[0].field").value("title"));
        }

        @Test
        @DisplayName("400 - Validation fails: null totalCopies")
        void addBook_400_NullTotalCopies() throws Exception {
            validBookDTO.setTotalCopies(null);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field=='totalCopies')]").exists());
        }

        @Test
        @DisplayName("400 - Validation fails: totalCopies exceeds 1000")
        void addBook_400_TotalCopiesTooHigh() throws Exception {
            validBookDTO.setTotalCopies(1001);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 - Missing required fields")
        void addBook_400_MissingRequiredFields() throws Exception {
            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new BookDTO())))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors").isArray());
        }
    }

    @Nested
    @DisplayName("PUT /api/books/{id}")
    class UpdateBookTests {

        @Test
        @DisplayName("200 - Updates book successfully")
        void updateBook_200() throws Exception {
            when(bookService.updateBook(eq(1L), any())).thenReturn(validBookDTO);

            mockMvc.perform(put("/api/books/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Clean Code"));
        }

        @Test
        @DisplayName("404 - Book not found for update")
        void updateBook_404() throws Exception {
            when(bookService.updateBook(eq(99L), any()))
                    .thenThrow(new ResourceNotFoundException("Book not found: 99"));

            mockMvc.perform(put("/api/books/99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookDTO)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/books/{id}")
    class DeleteBookTests {

        @Test
        @DisplayName("204 - Deletes book successfully")
        void deleteBook_204() throws Exception {
            doNothing().when(bookService).deleteBook(1L);

            mockMvc.perform(delete("/api/books/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("404 - Book not found for delete")
        void deleteBook_404() throws Exception {
            doThrow(new ResourceNotFoundException("Book not found: 99"))
                    .when(bookService).deleteBook(99L);

            mockMvc.perform(delete("/api/books/99"))
                    .andExpect(status().isNotFound());
        }
    }
}