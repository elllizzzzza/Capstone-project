package com.educationalSystem.entity.parts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Progress {
    private double completionRate;

    @ElementCollection
    @CollectionTable(
            name = "completed_lessons",
            joinColumns = @JoinColumn(name = "enrollment_id")
    )
    @Column(name = "lesson_id")
    private List<Lesson> completedLessons;
}
