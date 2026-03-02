package com.educationalSystem.controller;

import com.educationalSystem.dto.EnrollmentDTO;
import com.educationalSystem.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    @PostMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<EnrollmentDTO> enroll(@PathVariable Long studentId,
                                                @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(enrollmentService.enroll(studentId, courseId));
    }

    @PatchMapping("/student/{studentId}/course/{courseId}/lesson/{lessonId}/complete")
    public ResponseEntity<EnrollmentDTO> markLessonCompleted(@PathVariable Long studentId,
                                                             @PathVariable Long courseId,
                                                             @PathVariable Long lessonId) {
        return ResponseEntity.ok(enrollmentService.markLessonCompleted(studentId, courseId, lessonId));
    }
}