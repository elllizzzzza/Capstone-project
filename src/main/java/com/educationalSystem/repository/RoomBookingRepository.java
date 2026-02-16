package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
}
