package com.educationalSystem.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private Long lessonId;
    private String title;
    private int durationMinutes;
    private Long courseId;
}
