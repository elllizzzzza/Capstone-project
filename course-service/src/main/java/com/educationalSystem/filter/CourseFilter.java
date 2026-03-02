package com.educationalSystem.filter;

import com.educationalSystem.enums.CourseLevel;
import com.educationalSystem.enums.CourseType;
import lombok.Data;

import java.util.List;

@Data
public class CourseFilter {

    private String courseName;

    private String category;

    private List<CourseType> type;

    private List<CourseLevel> level;

    private Long instructorId;
}