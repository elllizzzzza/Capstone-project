package com.educationalSystem.dto;

import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDTO {

    private Long id;

    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 200, message = "Course name must be between 3 and 200 characters")
    private String courseName;

    @NotBlank(message = "Category is required")
    @Size(min = 2, max = 100, message = "Category must be between 2 and 100 characters")
    private String category;

    @NotNull(message = "Course type is required")
    private CourseType type;

    @NotNull(message = "Course level is required")
    private CourseLevel level;

    @NotNull(message = "Instructor ID is required")
    @Positive(message = "Instructor ID must be positive")
    private Long instructorId;

    private List<Long> lessonIds = new ArrayList<>();
    private List<Long> reviewIds = new ArrayList<>();
}