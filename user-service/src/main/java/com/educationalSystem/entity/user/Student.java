package com.educationalSystem.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("STUDENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User{
    private String university;
    private String uniId;
    private String cardDetails;
}
