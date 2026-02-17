package com.educationalSystem.service;

import com.educationalSystem.converter.EnrollmentConverter;
import com.educationalSystem.dto.EnrollmentDTO;
import com.educationalSystem.entity.parts.*;
import com.educationalSystem.entity.user.Student;
import com.educationalSystem.exception.BusinessException;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ProgressRepository progressRepository;
    private final EnrollmentConverter enrollmentConverter;

    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudent_Id(studentId).stream()
                .map(e -> enrollmentConverter.convertToDTO(e, new EnrollmentDTO()))
                .toList();
    }

    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourse_CourseId(courseId).stream()
                .map(e -> enrollmentConverter.convertToDTO(e, new EnrollmentDTO()))
                .toList();
    }

    @Transactional
    public EnrollmentDTO enroll(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudent_IdAndCourse_CourseId(studentId, courseId)) {
            throw new BusinessException("Student already enrolled in this course");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollmentRepository.save(enrollment);

        Progress progress = new Progress();
        progress.setEnrollment(enrollment);
        progress.setCompletionRate(0.0);
        progressRepository.save(progress);

        return enrollmentConverter.convertToDTO(enrollment, new EnrollmentDTO());
    }

    @Transactional
    public EnrollmentDTO markLessonCompleted(Long studentId, Long courseId, Long lessonId) {
        Enrollment enrollment = enrollmentRepository
                .findByStudent_IdAndCourse_CourseId(studentId, courseId)
                .orElseThrow(() -> new BusinessException("Student is not enrolled in this course"));

        Progress progress = enrollment.getProgress();
        boolean alreadyDone = progress.getCompletedLessons().stream()
                .anyMatch(l -> l.getLessonId().equals(lessonId));

        if (!alreadyDone) {
            Lesson lesson = enrollment.getCourse().getLessons().stream()
                    .filter(l -> l.getLessonId().equals(lessonId))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Lesson not in this course"));

            progress.getCompletedLessons().add(lesson);

            int total = enrollment.getCourse().getLessons().size();
            int completed = progress.getCompletedLessons().size();
            progress.setCompletionRate(total > 0 ? (double) completed / total * 100 : 0);
            progressRepository.save(progress);
        }

        return enrollmentConverter.convertToDTO(enrollment, new EnrollmentDTO());
    }
}