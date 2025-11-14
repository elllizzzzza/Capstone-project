package com.educationalSystem.entity.parts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBooking {
    private Long bookId;
    private Long roomId;
    private Long studentId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean active;
}
