package com.educationalSystem.service;

import com.educationalSystem.mapper.RoomBookingMapper;
import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.entity.user.Student;
import com.educationalSystem.enums.BookingStatus;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final RoomBookingMapper bookingConverter;

    public List<RoomBookingDTO> getBookingsByStudent(Long studentId) {
        return bookingRepository.findByStudent_Id(studentId).stream()
                .map(b -> bookingConverter.convertToDTO(b, new RoomBookingDTO()))
                .toList();
    }

    public List<RoomBookingDTO> getBookingsByInstructor(Long instructorId) {
        return bookingRepository.findByInstructor_Id(instructorId).stream()
                .map(b -> bookingConverter.convertToDTO(b, new RoomBookingDTO()))
                .toList();
    }

    public List<RoomBookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(b -> bookingConverter.convertToDTO(b, new RoomBookingDTO()))
                .toList();
    }

    @Transactional
    public RoomBookingDTO bookRoom(RoomBookingDTO dto) {
        if (dto.getStartTime().isAfter(dto.getEndTime()) || dto.getStartTime().equals(dto.getEndTime())) {
            throw new BusinessException("Start time must be before end time");
        }

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        List<RoomBooking> conflicts = bookingRepository.findConflicting(
                dto.getRoomId(), dto.getDate(), dto.getStartTime(), dto.getEndTime());
        if (!conflicts.isEmpty()) {
            throw new BusinessException("Room is already booked during this time slot");
        }

        RoomBooking booking = bookingConverter.convertToEntity(dto, new RoomBooking());
        booking.setRoom(room);
        booking.setStatus(BookingStatus.ACTIVE);

        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            booking.setStudent(student);
        } else if (dto.getInstructorId() != null) {
            Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Instructor not found"));
            booking.setInstructor(instructor);
        } else {
            throw new BusinessException("Either studentId or instructorId must be provided");
        }

        bookingRepository.save(booking);
        return bookingConverter.convertToDTO(booking, new RoomBookingDTO());
    }

    @Transactional
    public RoomBookingDTO cancelBooking(Long bookingId) {
        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BusinessException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCanceledAt(LocalDateTime.now());
        bookingRepository.save(booking);

        return bookingConverter.convertToDTO(booking, new RoomBookingDTO());
    }
}