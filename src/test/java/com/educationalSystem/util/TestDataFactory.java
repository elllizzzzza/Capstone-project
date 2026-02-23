package com.educationalSystem.util;

import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.dto.request.RegisterRequest;
import com.educationalSystem.entity.parts.Book;
import com.educationalSystem.entity.parts.BorrowRecord;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.entity.user.Student;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import com.educationalSystem.enums.Role;
import com.educationalSystem.enums.RoomType;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Centralized test data builders.
 * Use these in all tests to keep data consistent and DRY.
 */
public final class TestDataFactory {

    private TestDataFactory() {}

    // ─────────────────────────────────────────────────────────────────
    // Entity builders
    // ─────────────────────────────────────────────────────────────────

    public static Student buildStudent(Long id) {
        Student s = new Student();
        s.setId(id);
        s.setName("John");
        s.setSurname("Doe");
        s.setUsername("student_" + id);
        s.setEmail("student" + id + "@test.com");
        s.setPassword("encodedPwd");
        s.setRole(Role.STUDENT);
        s.setUniversity("MIT");
        s.setUniId("MIT00" + id);
        s.setActive(true);
        return s;
    }

    public static Instructor buildInstructor(Long id) {
        Instructor i = new Instructor();
        i.setId(id);
        i.setName("Prof.");
        i.setSurname("Smith");
        i.setUsername("instructor_" + id);
        i.setEmail("instructor" + id + "@test.com");
        i.setPassword("encodedPwd");
        i.setRole(Role.INSTRUCTOR);
        i.setAbout("Experienced educator");
        i.setProfession("Software Engineer");
        i.setActive(true);
        return i;
    }

    public static Book buildBook(Long id, int availableCopies) {
        Book b = new Book();
        b.setId(id);
        b.setTitle("Book Title " + id);
        b.setAuthor("Author " + id);
        b.setLanguage("English");
        b.setGenre("Programming");
        b.setTotalCopies(5);
        b.setAvailableCopies(availableCopies);
        return b;
    }

    public static Course buildCourse(Long id, Instructor instructor) {
        Course c = new Course();
        c.setCourseId(id);
        c.setCourseName("Course " + id);
        c.setCategory("Technology");
        c.setType(CourseType.VIDEO_BASED);
        c.setLevel(CourseLevel.BEGINNER);
        c.setInstructor(instructor);
        return c;
    }

    public static Room buildRoom(Long id) {
        Room r = new Room();
        r.setRoomId(id);
        r.setRoomNumber(100 + id.intValue());
        r.setFloor(1);
        r.setCapacity(30);
        r.setType(RoomType.CLASSROOM);
        return r;
    }

    public static BorrowRecord buildActiveBorrowRecord(Long id, Student student, Book book) {
        BorrowRecord br = new BorrowRecord();
        br.setBorrowRecordId(id);
        br.setStudent(student);
        br.setBook(book);
        br.setBorrowDate(LocalDate.now().minusDays(3));
        br.setDueDate(LocalDate.now().plusDays(11));
        // endDate is null → active
        return br;
    }

    public static BorrowRecord buildOverdueBorrowRecord(Long id, Student student, Book book) {
        BorrowRecord br = buildActiveBorrowRecord(id, student, book);
        br.setBorrowDate(LocalDate.now().minusDays(20));
        br.setDueDate(LocalDate.now().minusDays(6));
        return br;
    }

    // ─────────────────────────────────────────────────────────────────
    // DTO builders
    // ─────────────────────────────────────────────────────────────────

    public static BookDTO buildValidBookDTO() {
        BookDTO dto = new BookDTO();
        dto.setTitle("Clean Architecture");
        dto.setAuthor("Robert Martin");
        dto.setLanguage("English");
        dto.setGenre("Programming");
        dto.setTotalCopies(10);
        dto.setAvailableCopies(10);
        return dto;
    }

    public static RoomBookingDTO buildValidRoomBookingDTO(Long roomId, Long studentId) {
        RoomBookingDTO dto = new RoomBookingDTO();
        dto.setRoomId(roomId);
        dto.setStudentId(studentId);
        dto.setDate(LocalDate.now().plusDays(1));
        dto.setStartTime(LocalTime.of(9, 0));
        dto.setEndTime(LocalTime.of(11, 0));
        return dto;
    }

    public static RegisterRequest buildValidRegisterRequest(String username, Role role) {
        RegisterRequest req = new RegisterRequest();
        req.setName("Test");
        req.setSurname("User");
        req.setUsername(username);
        req.setEmail(username + "@test.com");
        req.setPassword("Password1!");
        req.setRole(role);
        if (role == Role.STUDENT) {
            req.setUniversity("Test University");
            req.setUniId("TU001");
        } else if (role == Role.INSTRUCTOR) {
            req.setAbout("Test about");
            req.setProfession("Developer");
        }
        return req;
    }
}