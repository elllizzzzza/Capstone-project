package com.educationalSystem.entity.user;


import com.educationalSystem.entity.parts.Course;
import com.educationalSystem.entity.parts.Review;
import com.educationalSystem.entity.parts.Room;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
