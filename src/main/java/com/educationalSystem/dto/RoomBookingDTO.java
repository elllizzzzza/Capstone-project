package com.educationalSystem.dto;

import com.educationalSystem.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class RoomBookingDTO {
    private Long bookingId;
    private Long studentId;
    private Long instructorId;
    private Long roomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime canceledAt;
    private BookingStatus status;

}
