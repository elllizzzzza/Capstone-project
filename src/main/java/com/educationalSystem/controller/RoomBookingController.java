package com.educationalSystem.controller;

import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.service.RoomBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class RoomBookingController {

    private final RoomBookingService bookingService;

    @GetMapping
    public ResponseEntity<List<RoomBookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<RoomBookingDTO>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(bookingService.getBookingsByStudent(studentId));
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<RoomBookingDTO>> getByInstructor(@PathVariable Long instructorId) {
        return ResponseEntity.ok(bookingService.getBookingsByInstructor(instructorId));
    }

    @PostMapping
    public ResponseEntity<RoomBookingDTO> bookRoom(@RequestBody RoomBookingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookRoom(dto));
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<RoomBookingDTO> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
}