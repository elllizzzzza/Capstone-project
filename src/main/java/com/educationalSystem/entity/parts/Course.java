package com.educationalSystem.entity.parts;

import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    private String courseName;
    private String category;

    @Enumerated(EnumType.STRING)
    private CourseType type;

    @Enumerated(EnumType.STRING)
    private CourseLevel level;

    @ManyToOne
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Review> reviews;
//
//    private double satisfactionFactor;
//    private int reviewNumber;
//    private LocalTime duration;
//    private int numOfLectures;
}
