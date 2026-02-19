package com.educationalSystem.service;

import com.educationalSystem.converter.CourseConverter;
import com.educationalSystem.dto.CourseDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.CourseRepository;
import com.educationalSystem.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final CourseConverter courseConverter;

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(c -> courseConverter.convertToDTO(c, new CourseDTO()))
                .toList();
    }

    public CourseDTO getCourseById(Long id) {
        Course course = findCourseOrThrow(id);
        return courseConverter.convertToDTO(course, new CourseDTO());
    }

    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(c -> courseConverter.convertToDTO(c, new CourseDTO()))
                .toList();
    }

    public List<CourseDTO> searchCourses(String keyword) {
        return courseRepository.findByCourseNameContainingIgnoreCase(keyword).stream()
                .map(c -> courseConverter.convertToDTO(c, new CourseDTO()))
                .toList();
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO dto) {
        Instructor instructor = instructorRepository.findById(dto.getInstructorId())
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found: " + dto.getInstructorId()));

        Course course = courseConverter.convertToEntity(dto, new Course());
        course.setInstructor(instructor);
        courseRepository.save(course);
        return courseConverter.convertToDTO(course, new CourseDTO());
    }

    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = findCourseOrThrow(id);
        courseConverter.convertToEntity(dto, course);
        courseRepository.save(course);
        return courseConverter.convertToDTO(course, new CourseDTO());
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found: " + id);
        }
        courseRepository.deleteById(id);
    }

    private Course findCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));
    }
}