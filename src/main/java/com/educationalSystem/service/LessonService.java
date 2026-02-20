package com.educationalSystem.service;

import com.educationalSystem.mapper.LessonMapper;
import com.educationalSystem.dto.LessonDTO;
import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Lesson;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.CourseRepository;
import com.educationalSystem.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    public List<LessonDTO> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourse_CourseId(courseId).stream()
                .map(l -> lessonMapper.convertToDTO(l, new LessonDTO()))
                .toList();
    }

    public LessonDTO getLessonById(Long id) {
        Lesson lesson = findOrThrow(id);
        return lessonMapper.convertToDTO(lesson, new LessonDTO());
    }

    @Transactional
    public LessonDTO createLesson(LessonDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + dto.getCourseId()));

        Lesson lesson = lessonMapper.convertToEntity(dto, new Lesson());
        lesson.setCourse(course);
        lessonRepository.save(lesson);
        return lessonMapper.convertToDTO(lesson, new LessonDTO());
    }

    @Transactional
    public LessonDTO updateLesson(Long id, LessonDTO dto) {
        Lesson lesson = findOrThrow(id);
        lessonMapper.convertToEntity(dto, lesson);
        lessonRepository.save(lesson);
        return lessonMapper.convertToDTO(lesson, new LessonDTO());
    }

    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found: " + id);
        }
        lessonRepository.deleteById(id);
    }

    private Lesson findOrThrow(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found: " + id));
    }
}