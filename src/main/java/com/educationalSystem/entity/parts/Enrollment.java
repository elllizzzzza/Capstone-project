package com.educationalSystem.entity.parts;

import com.educationalSystem.entity.user.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment {
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    private Progress progress;
}
