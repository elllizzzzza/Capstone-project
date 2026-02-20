package com.educationalSystem.entity.user;


import com.educationalSystem.entity.parts.Course;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("INSTRUCTOR")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor extends User {
    private String about;
    private String profession;

    @OneToMany(mappedBy = "instructor")
    private List<Course> myCourses = new ArrayList<>();
}
