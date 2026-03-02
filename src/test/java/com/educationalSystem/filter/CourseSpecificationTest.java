package com.educationalSystem.filter;

import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.user.Instructor;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import com.educationalSystem.repository.CourseRepository;
import com.educationalSystem.repository.InstructorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CourseSpecificationTest {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    InstructorRepository instructorRepository;

    private Instructor instructorA;
    private Instructor instructorB;

    @BeforeEach
    void setUp() {
        instructorA = instructorRepository.save(instructor("alice"));
        instructorB = instructorRepository.save(instructor("bob"));

        courseRepository.saveAll(List.of(
                course("Java Basics",      "Programming", CourseType.TEXT_BASED,    CourseLevel.BEGINNER,     instructorA),
                course("Java Advanced",    "Programming", CourseType.ASSIGNMENTS, CourseLevel.ADVANCED,     instructorA),
                course("Data Science 101", "Data",        CourseType.TEXT_BASED,    CourseLevel.BEGINNER,     instructorB),
                course("ML Engineering",   "Data",        CourseType.ASSIGNMENTS, CourseLevel.INTERMEDIATE, instructorB)
        ));
    }

    @Test
    void courseNameContains_partialCaseInsensitive() {
        var result = courseRepository.findAll(CourseSpecification.courseNameContains("java"));
        assertThat(result).hasSize(2);
    }

    @Test
    void categoryContains_partialMatch() {
        var result = courseRepository.findAll(CourseSpecification.categoryContains("data"));
        assertThat(result).hasSize(2);
    }

    @Test
    void typeIn_singleValue() {
        var result = courseRepository.findAll(CourseSpecification.typeIn(List.of(CourseType.ASSIGNMENTS)));
        assertThat(result).hasSize(2).allMatch(c -> c.getType() == CourseType.ASSIGNMENTS);
    }

    @Test
    void levelIn_multipleValues() {
        var result = courseRepository.findAll(
                CourseSpecification.levelIn(List.of(CourseLevel.BEGINNER, CourseLevel.ADVANCED)));
        assertThat(result).hasSize(3);
    }

    @Test
    void instructorIdEquals_filtersCorrectly() {
        var result = courseRepository.findAll(
                CourseSpecification.instructorIdEquals(instructorA.getId()));
        assertThat(result).hasSize(2).allMatch(c -> c.getInstructor().getId().equals(instructorA.getId()));
    }

    @Test
    void combined_typeAndCategory() {
        CourseFilter filter = new CourseFilter();
        filter.setType(List.of(CourseType.TEXT_BASED));
        filter.setCategory("data");

        var result = courseRepository.findAll(CourseSpecification.fromFilter(filter));
        assertThat(result).hasSize(1)
                .allMatch(c -> c.getType() == CourseType.TEXT_BASED && c.getCategory().equalsIgnoreCase("Data"));
    }

    @Test
    void emptyFilter_returnsAll() {
        assertThat(courseRepository.findAll(CourseSpecification.fromFilter(new CourseFilter()))).hasSize(4);
    }

    private Instructor instructor(String username) {
        Instructor i = new Instructor();
        i.setName(username); i.setSurname("Surname");
        i.setUsername(username); i.setEmail(username + "@test.com");
        i.setPassword("pass");
        return i;
    }

    private Course course(String name, String category, CourseType type,
                          CourseLevel level, Instructor instructor) {
        Course c = new Course();
        c.setCourseName(name); c.setCategory(category);
        c.setType(type); c.setLevel(level);
        c.setInstructor(instructor);
        return c;
    }
}