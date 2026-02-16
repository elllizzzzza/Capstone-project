package com.educationalSystem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InstructorDTO extends UserDTO {
    private String about;
    private String profession;
    private List<Long> myCoursesIds = new ArrayList<>();
}
