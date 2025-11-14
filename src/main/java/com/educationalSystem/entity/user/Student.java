package com.educationalSystem.entity.user;

import com.educationalSystem.entity.parts.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User{
    private Long studentId;
    private String university;
    private String uniId;
    private String cardDetails;
    private List<Enrollment> enrollments;
    private List<BorrowRecord> borrowHistory;
    private List<RoomBooking> bookings;

    public List<Course> viewCourses(){
        return new ArrayList<>();
    }

    public boolean enrollInCourse(Course course){
        return false;
    }

    public void accessLessons(Course course){

    }

    public boolean markLessonCompleted(Course course){
        return false;
    }

    public double viewProgress(Course course){
        return 0;
    }

    public void submitCourseReview(Course course, Review review){

    }

    public List<Course> browseLibraryCatalog(){
        return new ArrayList<>();
    }

    public void borrowBook(Book book){

    }

    public boolean returnBook(Book book){
        return false;
    }

    public List<Book> viewBorrowingHistory(){
        return new ArrayList<>();
    }

    public boolean bookRoom(Book book){
        return false;
    }

    public boolean cancelRoomBooking(RoomBooking booking){
        return false;
    }
}
