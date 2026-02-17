package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    List<RoomBooking> findByStudent_Id(Long studentId);
    List<RoomBooking> findByInstructor_Id(Long instructorId);

    @Query("""
        SELECT rb FROM RoomBooking rb
        WHERE rb.room.roomId = :roomId
          AND rb.date = :date
          AND rb.status = 'ACTIVE'
          AND rb.startTime < :endTime
          AND rb.endTime > :startTime
    """)
    List<RoomBooking> findConflicting(Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
