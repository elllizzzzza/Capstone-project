package com.educationalSystem.entity.parts;

import com.educationalSystem.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    private Long studentId;
    private Long instructorId;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime canceledAt;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.ACTIVE;
}
