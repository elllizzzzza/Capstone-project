package com.educationalSystem.dto;

import com.educationalSystem.enums.BookingStatus;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class RoomBookingDTO {

    private Long bookingId;

    @Positive(message = "Student ID must be positive")
    private Long studentId;

    @Positive(message = "Instructor ID must be positive")
    private Long instructorId;

    @NotNull(message = "Room ID is required")
    @Positive(message = "Room ID must be positive")
    private Long roomId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    private LocalDateTime canceledAt;
    private BookingStatus status;
}