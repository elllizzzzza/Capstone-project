package com.educationalSystem.entity.parts;

import com.educationalSystem.entity.user.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Room room;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Boolean active;
}
