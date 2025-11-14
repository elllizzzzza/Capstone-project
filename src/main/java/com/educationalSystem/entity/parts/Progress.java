package com.educationalSystem.entity.parts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Progress {
    private double completionRate;
    private List<Lesson> completedLessons;
}
