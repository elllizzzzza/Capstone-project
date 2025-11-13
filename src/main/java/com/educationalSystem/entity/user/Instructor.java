package com.educationalSystem.entity.user;


import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Review;
import com.educationalSystem.entity.parts.Room;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor extends User {
    private Long instructorId;
    private String about;
    private String profession;
    private List<Course> myCourses;

    public void createCourse(Course course){

    }

    public void manageCourse(Course course){

    }

    public void editCourse(Course course){

    }

    public void deleteCourse(Course course){

    }

    public List<Student> viewEnrolledStudents(Course course){
        return new ArrayList<>();
    }

    public double trackStudentProgress(Student student){
        return 0;
    }

    public void respondToReview(Review review){

    }

    public void bookRoom(Room room){

    }
}
