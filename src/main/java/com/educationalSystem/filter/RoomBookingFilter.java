package com.educationalSystem.filter;

import com.educationalSystem.enums.BookingStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
public class RoomBookingFilter {

    private List<BookingStatus> status;

    private Long roomId;

    private Long studentId;

    private Long instructorId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateTo;
}