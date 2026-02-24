package com.educationalSystem.filter;

import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.entity.user.Student;
import com.educationalSystem.enums.BookingStatus;
import com.educationalSystem.enums.RoomType;
import com.educationalSystem.repository.RoomBookingRepository;
import com.educationalSystem.repository.RoomRepository;
import com.educationalSystem.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomBookingSpecificationTest {

    @Autowired
    RoomBookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    StudentRepository studentRepository;

    private Room roomA;
    private Room roomB;
    private Student student;

    @BeforeEach
    void setUp() {
        roomA = roomRepository.save(room(101, RoomType.CLASSROOM));
        roomB = roomRepository.save(room(202, RoomType.LAB));
        student = studentRepository.save(student("john"));

        bookingRepository.saveAll(List.of(
                booking(roomA, student, LocalDate.of(2024, 1, 10), BookingStatus.ACTIVE),
                booking(roomA, student, LocalDate.of(2024, 3, 15), BookingStatus.CANCELLED),
                booking(roomB, student, LocalDate.of(2024, 6, 20), BookingStatus.ACTIVE),
                booking(roomB, student, LocalDate.of(2024, 9, 5),  BookingStatus.ACTIVE)
        ));
    }

    @Test
    void statusIn_active_returnsThree() {
        var result = bookingRepository.findAll(
                RoomBookingSpecification.statusIn(List.of(BookingStatus.ACTIVE)));
        assertThat(result).hasSize(3).allMatch(b -> b.getStatus() == BookingStatus.ACTIVE);
    }

    @Test
    void statusIn_multipleValues_returnsAll() {
        var result = bookingRepository.findAll(
                RoomBookingSpecification.statusIn(List.of(BookingStatus.ACTIVE, BookingStatus.CANCELLED)));
        assertThat(result).hasSize(4);
    }

    @Test
    void roomIdEquals_filtersCorrectly() {
        var result = bookingRepository.findAll(
                RoomBookingSpecification.roomIdEquals(roomA.getRoomId()));
        assertThat(result).hasSize(2)
                .allMatch(b -> b.getRoom().getRoomId().equals(roomA.getRoomId()));
    }

    @Test
    void dateFrom_greaterThanOrEqual() {
        var result = bookingRepository.findAll(
                RoomBookingSpecification.dateFrom(LocalDate.of(2024, 6, 1)));
        assertThat(result).hasSize(2)
                .allMatch(b -> !b.getDate().isBefore(LocalDate.of(2024, 6, 1)));
    }

    @Test
    void dateTo_lessThanOrEqual() {
        var result = bookingRepository.findAll(
                RoomBookingSpecification.dateTo(LocalDate.of(2024, 3, 31)));
        assertThat(result).hasSize(2)
                .allMatch(b -> !b.getDate().isAfter(LocalDate.of(2024, 3, 31)));
    }

    @Test
    void combined_statusAndDateRange() {
        RoomBookingFilter filter = new RoomBookingFilter();
        filter.setStatus(List.of(BookingStatus.ACTIVE));
        filter.setDateFrom(LocalDate.of(2024, 6, 1));
        filter.setDateTo(LocalDate.of(2024, 12, 31));

        var result = bookingRepository.findAll(RoomBookingSpecification.fromFilter(filter));
        assertThat(result).hasSize(2)
                .allMatch(b -> b.getStatus() == BookingStatus.ACTIVE);
    }

    @Test
    void emptyFilter_returnsAll() {
        assertThat(bookingRepository.findAll(
                RoomBookingSpecification.fromFilter(new RoomBookingFilter()))).hasSize(4);
    }

    private Room room(int number, RoomType type) {
        Room r = new Room();
        r.setRoomNumber(number); r.setFloor(1);
        r.setCapacity(30); r.setType(type);
        return r;
    }

    private Student student(String username) {
        Student s = new Student();
        s.setName(username); s.setSurname("Doe");
        s.setUsername(username); s.setEmail(username + "@test.com");
        s.setPassword("pass");
        return s;
    }

    private RoomBooking booking(Room room, Student student, LocalDate date, BookingStatus status) {
        RoomBooking b = new RoomBooking();
        b.setRoom(room); b.setStudent(student);
        b.setDate(date);
        b.setStartTime(LocalTime.of(9, 0));
        b.setEndTime(LocalTime.of(10, 0));
        b.setStatus(status);
        return b;
    }
}