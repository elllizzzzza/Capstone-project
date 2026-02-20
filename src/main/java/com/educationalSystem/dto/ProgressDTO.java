package com.educationalSystem.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProgressDTO {
    private Long id;
    private Long enrollmentId;
    private double completionRate;
    private List<Long> completedLessonIds = new ArrayList<>();
}
