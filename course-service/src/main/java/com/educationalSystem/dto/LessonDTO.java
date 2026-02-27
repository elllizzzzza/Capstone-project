package com.educationalSystem.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class LessonDTO {

    private Long lessonId;

    @NotBlank(message = "Lesson title is required")
    @Size(min = 3, max = 200, message = "Lesson title must be between 3 and 200 characters")
    private String title;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 480, message = "Duration cannot exceed 8 hours (480 minutes)")
    private Integer durationMinutes;

    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be positive")
    private Long courseId;
}