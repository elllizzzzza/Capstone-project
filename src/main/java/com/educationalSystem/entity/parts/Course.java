package com.educationalSystem.entity.parts;

import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long courseId;
    private String courseName;
    private List<CourseType> type;
    private String category;
    private Instructor instructor;
    private List<Lesson> lessons;
    private double satisfactionFactor;
    private int reviewNumber;
    private List<Review> reviews;
    private LocalTime duration;
    private int numOfLectures;
    private CourseLevel level;
}
