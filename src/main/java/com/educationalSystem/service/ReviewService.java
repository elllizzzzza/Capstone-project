package com.educationalSystem.service;

import com.educationalSystem.mapper.ReviewMapper;
import com.educationalSystem.dto.ReviewDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Review;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewMapper reviewMapper;

    public List<ReviewDTO> getReviewsByCourse(Long courseId) {
        return reviewRepository.findByCourse_CourseId(courseId).stream()
                .map(r -> reviewMapper.convertToDTO(r, new ReviewDTO()))
                .toList();
    }

    @Transactional
    public ReviewDTO submitReview(ReviewDTO dto) {
        if (!enrollmentRepository.existsByStudent_IdAndCourse_CourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new BusinessException("You must be enrolled in the course to leave a review");
        }
        if (reviewRepository.existsByStudent_IdAndCourse_CourseId(dto.getStudentId(), dto.getCourseId())) {
            throw new BusinessException("You have already reviewed this course");
        }
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new BusinessException("Rating must be between 1 and 5");
        }

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Review review = reviewMapper.convertToEntity(dto, new Review());
        review.setStudent(student);
        review.setCourse(course);
        review.setDate(LocalDate.now());
        reviewRepository.save(review);

        return reviewMapper.convertToDTO(review, new ReviewDTO());
    }

    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found: " + id);
        }
        reviewRepository.deleteById(id);
    }
}