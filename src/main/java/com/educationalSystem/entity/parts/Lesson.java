package com.educationalSystem.entity.parts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private Long lessonId;
    private String title;
    private LocalTime duration;
    private Boolean isCompleted;
}
