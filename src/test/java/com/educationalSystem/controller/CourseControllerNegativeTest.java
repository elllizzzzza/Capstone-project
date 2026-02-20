package com.educationalSystem.controller;

import com.educationalSystem.dto.CourseDTO;
import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import com.educationalSystem.repository.*;
import com.educationalSystem.entity.user.Instructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CourseControllerNegativeTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Long instructorId;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
        instructorRepository.deleteAll();

        Instructor instructor = new Instructor();
        instructor.setName("John");
        instructor.setSurname("Doe");
        instructor.setUsername("johndoe");
        instructor.setEmail("john@uni.edu");
        instructor.setPassword("password");
        instructor.setAbout("PhD in CS");
        instructor.setProfession("Professor");
        instructorId = instructorRepository.save(instructor).getId();
    }

    @Test
    void createCourse_ShouldReturn400_WhenCourseNameIsBlank() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("");  // Blank name
        dto.setCategory("CS");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("validation-error")))
                .andExpect(jsonPath("$.title", is("Validation Failed")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.errors[?(@.field == 'courseName')].message",
                        hasItem(containsString("required"))));
    }

    @Test
    void createCourse_ShouldReturn400_WhenCourseNameTooShort() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("AB");  // Too short (min 3)
        dto.setCategory("CS");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'courseName')].message",
                        hasItem(containsString("between 3 and 200"))));
    }

    @Test
    void createCourse_ShouldReturn400_WhenMultipleFieldsInvalid() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("");  // Invalid
        dto.setCategory("");    // Invalid
        dto.setType(null);      // Invalid
        dto.setLevel(null);     // Invalid
        dto.setInstructorId(null); // Invalid

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(5)))  // All 5 fields have errors
                .andExpect(jsonPath("$.errors[*].field",
                        containsInAnyOrder("courseName", "category", "type", "level", "instructorId")));
    }

    @Test
    void createCourse_ShouldReturn400_WhenInstructorIdNegative() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Valid Course");
        dto.setCategory("CS");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(-1L);  // Negative ID

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'instructorId')].message",
                        hasItem(containsString("positive"))));
    }

    @Test
    void createCourse_ShouldReturn404_WhenInstructorNotFound() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Valid Course");
        dto.setCategory("CS");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(99999L);  // Non-existent instructor

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("resource-not-found")))
                .andExpect(jsonPath("$.title", is("Resource Not Found")))
                .andExpect(jsonPath("$.detail", containsString("Instructor not found")));
    }

    @Test
    void getCourseById_ShouldReturn404_WhenCourseNotFound() throws Exception {
        mockMvc.perform(get("/api/courses/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("resource-not-found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", containsString("Course not found")));
    }

    @Test
    void updateCourse_ShouldReturn404_WhenCourseNotFound() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Updated Course");
        dto.setCategory("CS");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        mockMvc.perform(put("/api/courses/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteCourse_ShouldReturn404_WhenCourseNotFound() throws Exception {
        mockMvc.perform(delete("/api/courses/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCourse_ShouldReturn400_WhenCategoryTooLong() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Valid Course");
        dto.setCategory("A".repeat(101));  // Exceeds max 100
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'category')].message",
                        hasItem(containsString("100 characters"))));
    }
}