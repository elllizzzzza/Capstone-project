package com.educationalSystem.entity.user;

import com.educationalSystem.entity.parts.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<BorrowRecord> borrowHistory = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<RoomBooking> bookings;

}
