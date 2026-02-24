package com.educationalSystem.controller;

import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.dto.response.PagedResponse;
import com.educationalSystem.enums.BookingStatus;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.filter.RoomBookingFilter;
import com.educationalSystem.service.RoomBookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoomBookingController.class)
@DisplayName("RoomBookingController Integration Tests")
class RoomBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoomBookingService bookingService;

    private RoomBookingDTO validBookingDTO;

    @BeforeEach
    void setUp() {
        validBookingDTO = new RoomBookingDTO();
        validBookingDTO.setBookingId(1L);
        validBookingDTO.setRoomId(1L);
        validBookingDTO.setStudentId(1L);
        validBookingDTO.setDate(LocalDate.now().plusDays(1));
        validBookingDTO.setStartTime(LocalTime.of(10, 0));
        validBookingDTO.setEndTime(LocalTime.of(12, 0));
        validBookingDTO.setStatus(BookingStatus.ACTIVE);
    }

    @Nested
    @DisplayName("GET /api/bookings")
    class GetAllBookingsTests {

        @Test
        @DisplayName("200 - Returns all bookings")
        void getAllBookings_200() throws Exception {
            Page<RoomBookingDTO> page = new PageImpl<>(List.of(validBookingDTO));

            when(bookingService.getAllBookings(any(RoomBookingFilter.class), any(Pageable.class)))
                    .thenReturn(PagedResponse.from(page));
            mockMvc.perform(get("/api/bookings"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].roomId").value(1L))
                    .andExpect(jsonPath("$.content[0].status").value("ACTIVE"));
        }
    }

    @Nested
    @DisplayName("GET /api/bookings/student/{studentId}")
    class GetByStudentTests {

        @Test
        @DisplayName("200 - Returns student bookings")
        void getByStudent_200() throws Exception {
            when(bookingService.getBookingsByStudent(1L)).thenReturn(List.of(validBookingDTO));

            mockMvc.perform(get("/api/bookings/student/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].studentId").value(1));
        }

        @Test
        @DisplayName("200 - Empty list when no bookings")
        void getByStudent_EmptyList() throws Exception {
            when(bookingService.getBookingsByStudent(1L)).thenReturn(List.of());

            mockMvc.perform(get("/api/bookings/student/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("POST /api/bookings")
    class BookRoomTests {

        @Test
        @DisplayName("201 - Books room successfully for student")
        void bookRoom_201() throws Exception {
            when(bookingService.bookRoom(any())).thenReturn(validBookingDTO);

            mockMvc.perform(post("/api/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookingDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.roomId").value(1))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("400 - Start time after end time")
        void bookRoom_400_InvalidTimeSlot() throws Exception {
            when(bookingService.bookRoom(any()))
                    .thenThrow(new BusinessException("Start time must be before end time"));

            mockMvc.perform(post("/api/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookingDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("Start time must be before end time"));
        }

        @Test
        @DisplayName("400 - Room already booked (conflict)")
        void bookRoom_400_Conflict() throws Exception {
            when(bookingService.bookRoom(any()))
                    .thenThrow(new BusinessException("Room is already booked during this time slot"));

            mockMvc.perform(post("/api/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookingDTO)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("Room is already booked during this time slot"));
        }

        @Test
        @DisplayName("404 - Room not found")
        void bookRoom_404_RoomNotFound() throws Exception {
            when(bookingService.bookRoom(any()))
                    .thenThrow(new ResourceNotFoundException("Room not found"));

            mockMvc.perform(post("/api/bookings")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validBookingDTO)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PATCH /api/bookings/{bookingId}/cancel")
    class CancelBookingTests {

        @Test
        @DisplayName("200 - Cancels booking successfully")
        void cancelBooking_200() throws Exception {
            RoomBookingDTO cancelled = new RoomBookingDTO();
            cancelled.setBookingId(1L);
            cancelled.setStatus(BookingStatus.CANCELLED);
            when(bookingService.cancelBooking(1L)).thenReturn(cancelled);

            mockMvc.perform(patch("/api/bookings/1/cancel"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CANCELLED"));
        }

        @Test
        @DisplayName("404 - Booking not found")
        void cancelBooking_404() throws Exception {
            when(bookingService.cancelBooking(99L))
                    .thenThrow(new ResourceNotFoundException("Booking not found: 99"));

            mockMvc.perform(patch("/api/bookings/99/cancel"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("Booking not found: 99"));
        }

        @Test
        @DisplayName("400 - Booking already cancelled")
        void cancelBooking_400_AlreadyCancelled() throws Exception {
            when(bookingService.cancelBooking(1L))
                    .thenThrow(new BusinessException("Booking is already cancelled"));

            mockMvc.perform(patch("/api/bookings/1/cancel"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.detail").value("Booking is already cancelled"));
        }
    }
}