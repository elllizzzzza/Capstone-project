package com.educationalSystem.dto;

import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseDTO {
    private Long id;
    private String courseName;
    private String category;
    private CourseType type;
    private CourseLevel level;
    private Long instructorId;
    private List<Long> lessonsIds = new ArrayList<>();
    private List<Long> reviewsIds = new ArrayList<>();

}
