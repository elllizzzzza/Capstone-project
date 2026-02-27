package com.educationalSystem.entity.user;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("INSTRUCTOR")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor extends User {
    private String about;
    private String profession;
}
