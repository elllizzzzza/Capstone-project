package com.educationalSystem.entity.parts;

import com.educationalSystem.entity.user.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    private Student student;
    private Course course;
    private String comment;
    private int rating;
    private LocalDate date;
}
