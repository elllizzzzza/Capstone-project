package com.educationalSystem.service;

import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.entity.user.Student;
import com.educationalSystem.enums.BookingStatus;
import com.educationalSystem.enums.RoomType;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.mapper.RoomBookingMapper;
import com.educationalSystem.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomBookingService Unit Tests")
class RoomBookingServiceTest {

    @Mock
    private RoomBookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private RoomBookingMapper bookingConverter;

    @InjectMocks
    private RoomBookingService roomBookingService;

    private Room room;
    private Student student;
    private Instructor instructor;
    private RoomBooking roomBooking;
    private RoomBookingDTO roomBookingDTO;

    @BeforeEach
    void setUp() {
        room = new Room();
        room.setRoomId(1L);
        room.setRoomNumber(101);
        room.setCapacity(30);
        room.setType(RoomType.CLASSROOM);

        student = new Student();
        student.setId(1L);
        student.setUsername("student1");

        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setUsername("instructor1");

        roomBooking = new RoomBooking();
        roomBooking.setBookingId(1L);
        roomBooking.setRoom(room);
        roomBooking.setStudent(student);
        roomBooking.setStatus(BookingStatus.ACTIVE);
        roomBooking.setDate(LocalDate.now().plusDays(1));
        roomBooking.setStartTime(LocalTime.of(10, 0));
        roomBooking.setEndTime(LocalTime.of(12, 0));

        roomBookingDTO = new RoomBookingDTO();
        roomBookingDTO.setBookingId(1L);
        roomBookingDTO.setRoomId(1L);
        roomBookingDTO.setStudentId(1L);
        roomBookingDTO.setDate(LocalDate.now().plusDays(1));
        roomBookingDTO.setStartTime(LocalTime.of(10, 0));
        roomBookingDTO.setEndTime(LocalTime.of(12, 0));
    }


    @Nested
    @DisplayName("bookRoom()")
    class BookRoomTests {

        @Test
        @DisplayName("Should book room for student successfully")
        void bookRoom_ForStudent_Success() {
            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(bookingRepository.findConflicting(anyLong(), any(), any(), any()))
                    .thenReturn(List.of());
            when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
            when(bookingConverter.convertToEntity(any(), any())).thenReturn(roomBooking);
            when(bookingConverter.convertToDTO(any(), any())).thenReturn(roomBookingDTO);

            RoomBookingDTO result = roomBookingService.bookRoom(roomBookingDTO);

            assertThat(result).isNotNull();
            verify(bookingRepository).save(any(RoomBooking.class));
        }

        @Test
        @DisplayName("Should book room for instructor successfully")
        void bookRoom_ForInstructor_Success() {
            roomBookingDTO.setStudentId(null);
            roomBookingDTO.setInstructorId(1L);

            RoomBooking instructorBooking = new RoomBooking();
            instructorBooking.setRoom(room);
            instructorBooking.setInstructor(instructor);
            instructorBooking.setStartTime(roomBookingDTO.getStartTime());
            instructorBooking.setEndTime(roomBookingDTO.getEndTime());

            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(bookingRepository.findConflicting(anyLong(), any(), any(), any()))
                    .thenReturn(List.of());
            when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
            when(bookingConverter.convertToEntity(any(), any())).thenReturn(instructorBooking);
            when(bookingConverter.convertToDTO(any(), any())).thenReturn(roomBookingDTO);

            RoomBookingDTO result = roomBookingService.bookRoom(roomBookingDTO);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should throw BusinessException when start time equals end time")
        void bookRoom_StartTimeEqualsEndTime() {
            roomBookingDTO.setStartTime(LocalTime.of(10, 0));
            roomBookingDTO.setEndTime(LocalTime.of(10, 0));

            assertThatThrownBy(() -> roomBookingService.bookRoom(roomBookingDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Start time must be before end time");
        }

        @Test
        @DisplayName("Should throw BusinessException when start time is after end time")
        void bookRoom_StartAfterEndTime() {
            roomBookingDTO.setStartTime(LocalTime.of(14, 0));
            roomBookingDTO.setEndTime(LocalTime.of(10, 0));

            assertThatThrownBy(() -> roomBookingService.bookRoom(roomBookingDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Start time must be before end time");
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when room not found")
        void bookRoom_RoomNotFound() {
            when(roomRepository.findById(99L)).thenReturn(Optional.empty());
            roomBookingDTO.setRoomId(99L);

            assertThatThrownBy(() -> roomBookingService.bookRoom(roomBookingDTO))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Room not found");
        }

        @Test
        @DisplayName("Should throw BusinessException when room has conflicting booking")
        void bookRoom_ConflictingBooking() {
            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(bookingRepository.findConflicting(anyLong(), any(), any(), any()))
                    .thenReturn(List.of(roomBooking));

            assertThatThrownBy(() -> roomBookingService.bookRoom(roomBookingDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("already booked during this time slot");
        }

        @Test
        @DisplayName("Should throw BusinessException when neither studentId nor instructorId provided")
        void bookRoom_NoBookerProvided() {
            roomBookingDTO.setStudentId(null);
            roomBookingDTO.setInstructorId(null);

            when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
            when(bookingRepository.findConflicting(anyLong(), any(), any(), any()))
                    .thenReturn(List.of());
            when(bookingConverter.convertToEntity(any(), any())).thenReturn(roomBooking);

            assertThatThrownBy(() -> roomBookingService.bookRoom(roomBookingDTO))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Either studentId or instructorId must be provided");
        }
    }

    @Nested
    @DisplayName("cancelBooking()")
    class CancelBookingTests {

        @Test
        @DisplayName("Should cancel active booking successfully")
        void cancelBooking_Success() {
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(roomBooking));
            when(bookingConverter.convertToDTO(any(), any())).thenReturn(roomBookingDTO);

            RoomBookingDTO result = roomBookingService.cancelBooking(1L);

            assertThat(result).isNotNull();
            assertThat(roomBooking.getStatus()).isEqualTo(BookingStatus.CANCELLED);
            assertThat(roomBooking.getCanceledAt()).isNotNull();
            verify(bookingRepository).save(roomBooking);
        }

        @Test
        @DisplayName("Should throw ResourceNotFoundException when booking not found")
        void cancelBooking_NotFound() {
            when(bookingRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> roomBookingService.cancelBooking(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Booking not found");
        }

        @Test
        @DisplayName("Should throw BusinessException when booking already cancelled")
        void cancelBooking_AlreadyCancelled() {
            roomBooking.setStatus(BookingStatus.CANCELLED);
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(roomBooking));

            assertThatThrownBy(() -> roomBookingService.cancelBooking(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("already cancelled");
        }
    }

    @Nested
    @DisplayName("getBookings()")
    class GetBookingsTests {

        @Test
        @DisplayName("Should return bookings for student")
        void getBookingsByStudent_ReturnsList() {
            when(bookingRepository.findByStudent_Id(1L)).thenReturn(List.of(roomBooking));
            when(bookingConverter.convertToDTO(any(), any())).thenReturn(roomBookingDTO);

            List<RoomBookingDTO> result = roomBookingService.getBookingsByStudent(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return bookings for instructor")
        void getBookingsByInstructor_ReturnsList() {
            RoomBooking instrBooking = new RoomBooking();
            instrBooking.setInstructor(instructor);
            when(bookingRepository.findByInstructor_Id(1L)).thenReturn(List.of(instrBooking));
            when(bookingConverter.convertToDTO(any(), any())).thenReturn(roomBookingDTO);

            List<RoomBookingDTO> result = roomBookingService.getBookingsByInstructor(1L);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Should return all bookings")
        void getAllBookings_ReturnsList() {
            when(bookingRepository.findAll()).thenReturn(List.of(roomBooking));
            when(bookingConverter.convertToDTO(any(), any())).thenReturn(roomBookingDTO);

            List<RoomBookingDTO> result = roomBookingService.getAllBookings();

            assertThat(result).hasSize(1);
        }
    }
}