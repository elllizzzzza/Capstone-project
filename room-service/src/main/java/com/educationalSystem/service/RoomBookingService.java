package com.educationalSystem.service;

import com.educationalSystem.client.UserClient;
import com.educationalSystem.dto.response.PagedResponse;
import com.educationalSystem.filter.RoomBookingFilter;
import com.educationalSystem.filter.RoomBookingSpecification;
import com.educationalSystem.mapper.RoomBookingMapper;
import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.enums.BookingStatus;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomBookingService {

    private final RoomBookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final UserClient userClient;
    private final RoomBookingMapper bookingConverter;

    public List<RoomBookingDTO> getBookingsByStudent(Long studentId) {
        return bookingRepository.findByStudentId(studentId).stream()
                .map(b -> bookingConverter.mapToDTO(b, new RoomBookingDTO()))
                .toList();
    }

    public List<RoomBookingDTO> getBookingsByInstructor(Long instructorId) {
        return bookingRepository.findByInstructorId(instructorId).stream()
                .map(b -> bookingConverter.mapToDTO(b, new RoomBookingDTO()))
                .toList();
    }

    public PagedResponse<RoomBookingDTO> getAllBookings(RoomBookingFilter filter, Pageable pageable) {
        return PagedResponse.from(
                bookingRepository.findAll(RoomBookingSpecification.fromFilter(filter), pageable)
                        .map(b -> bookingConverter.mapToDTO(b, new RoomBookingDTO()))
        );
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

        RoomBooking booking = bookingConverter.mapToEntity(dto, new RoomBooking());
        booking.setRoom(room);
        booking.setStatus(BookingStatus.ACTIVE);

        if (dto.getStudentId() != null) {
            if (!userClient.userExists(dto.getStudentId())) {
                throw new ResourceNotFoundException("Student not found");
            }

            booking.setStudentId(dto.getStudentId());
        } else if (dto.getInstructorId() != null) {
            if (!userClient.userExists(dto.getInstructorId())){
                throw new ResourceNotFoundException("Instructor not found");
            }
            booking.setInstructorId(dto.getInstructorId());
        } else {
            throw new BusinessException("Either studentId or instructorId must be provided");
        }

        bookingRepository.save(booking);
        return bookingConverter.mapToDTO(booking, new RoomBookingDTO());
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

        return bookingConverter.mapToDTO(booking, new RoomBookingDTO());
    }
}