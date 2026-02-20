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
class CourseControllerIntegrationTest {

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
    void getAllCourses_ShouldReturnEmptyList_WhenNoCoursesExist() throws Exception {
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createCourse_ShouldReturnCreated_WhenValidRequest() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Algorithms");
        dto.setCategory("Computer Science");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseName", is("Algorithms")))
                .andExpect(jsonPath("$.category", is("Computer Science")))
                .andExpect(jsonPath("$.type", is("VIDEO_BASED")))
                .andExpect(jsonPath("$.level", is("BEGINNER")))
                .andExpect(jsonPath("$.instructorId", is(instructorId.intValue())));
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenExists() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Data Structures");
        dto.setCategory("Computer Science");
        dto.setType(CourseType.TEXT_BASED);
        dto.setLevel(CourseLevel.INTERMEDIATE);
        dto.setInstructorId(instructorId);

        String createResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CourseDTO created = objectMapper.readValue(createResponse, CourseDTO.class);

        mockMvc.perform(get("/api/courses/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName", is("Data Structures")))
                .andExpect(jsonPath("$.id", is(created.getId().intValue())));
    }

    @Test
    void getCourseById_ShouldReturn404_WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/courses/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCourse_ShouldReturnUpdated_WhenValidRequest() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("Original Name");
        dto.setCategory("Math");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        String createResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        CourseDTO created = objectMapper.readValue(createResponse, CourseDTO.class);

        dto.setCourseName("Updated Name");
        dto.setLevel(CourseLevel.ADVANCED);

        mockMvc.perform(put("/api/courses/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseName", is("Updated Name")))
                .andExpect(jsonPath("$.level", is("ADVANCED")));
    }

    @Test
    void deleteCourse_ShouldReturnNoContent_WhenExists() throws Exception {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName("To Delete");
        dto.setCategory("Test");
        dto.setType(CourseType.QUIZ);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);

        String createResponse = mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        CourseDTO created = objectMapper.readValue(createResponse, CourseDTO.class);

        mockMvc.perform(delete("/api/courses/" + created.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/courses/" + created.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllCourses_ShouldReturnMultipleCourses() throws Exception {
        CourseDTO dto1 = createCourseDTO("Course 1", instructorId);
        CourseDTO dto2 = createCourseDTO("Course 2", instructorId);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto1)));

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto2)));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].courseName", is("Course 1")))
                .andExpect(jsonPath("$[1].courseName", is("Course 2")));
    }

    @Test
    void getCoursesByInstructor_ShouldReturnOnlyInstructorCourses() throws Exception {
        CourseDTO dto = createCourseDTO("Instructor Course", instructorId);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        mockMvc.perform(get("/api/courses/instructor/" + instructorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].instructorId", is(instructorId.intValue())));
    }

    private CourseDTO createCourseDTO(String name, Long instructorId) {
        CourseDTO dto = new CourseDTO();
        dto.setCourseName(name);
        dto.setCategory("Test Category");
        dto.setType(CourseType.VIDEO_BASED);
        dto.setLevel(CourseLevel.BEGINNER);
        dto.setInstructorId(instructorId);
        return dto;
    }
}