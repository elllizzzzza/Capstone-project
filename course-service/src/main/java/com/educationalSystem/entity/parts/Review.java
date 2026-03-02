package com.educationalSystem.entity.parts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private String comment;
    private int rating;
    private LocalDate date;
    private String instructorReply;
    private LocalDate repliedAt;
}
