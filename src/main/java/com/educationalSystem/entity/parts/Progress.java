package com.educationalSystem.entity.parts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private double completionRate;

    @OneToOne
    @JoinColumn(name = "enrollment_id")
    private Enrollment enrollment;

    @ManyToMany
    @JoinTable(
            name= "completed_lessons",
            joinColumns = @JoinColumn(name = "progress_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private List<Lesson> completedLessons = new ArrayList<>();
}
