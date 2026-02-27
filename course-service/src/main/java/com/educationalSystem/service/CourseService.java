package com.educationalSystem.service;

import com.educationalSystem.client.UserClient;
import com.educationalSystem.dto.response.PagedResponse;
import com.educationalSystem.filter.CourseFilter;
import com.educationalSystem.filter.CourseSpecification;
import com.educationalSystem.mapper.CourseMapper;
import com.educationalSystem.dto.CourseDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserClient userClient;
    private final CourseMapper courseMapper;

    public PagedResponse<CourseDTO> getAllCourses(CourseFilter filter, Pageable pageable) {
        return PagedResponse.from(
                courseRepository.findAll(CourseSpecification.fromFilter(filter), pageable)
                        .map(c -> courseMapper.mapToDTO(c, new CourseDTO()))
        );
    }

    public CourseDTO getCourseById(Long id) {
        return courseMapper.mapToDTO(findCourseOrThrow(id), new CourseDTO());
    }

    public List<CourseDTO> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId).stream()
                .map(c -> courseMapper.mapToDTO(c, new CourseDTO()))
                .toList();
    }

    public List<CourseDTO> searchCourses(String keyword) {
        return courseRepository.findByCourseNameContainingIgnoreCase(keyword).stream()
                .map(c -> courseMapper.mapToDTO(c, new CourseDTO()))
                .toList();
    }

    @Transactional
    public CourseDTO createCourse(CourseDTO dto) {
        if (!userClient.userExists(dto.getInstructorId())) {
            throw new ResourceNotFoundException("Instructor not found: " + dto.getInstructorId());
        }

        Course course = courseMapper.mapToEntity(dto, new Course());
        course.setInstructorId(dto.getInstructorId());
        courseRepository.save(course);
        return courseMapper.mapToDTO(course, new CourseDTO());
    }

    @Transactional
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = findCourseOrThrow(id);
        courseMapper.mapToEntity(dto, course);
        courseRepository.save(course);
        return courseMapper.mapToDTO(course, new CourseDTO());
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