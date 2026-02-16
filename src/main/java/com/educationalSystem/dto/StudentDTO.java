package com.educationalSystem.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentDTO extends UserDTO {
    private String university;
    private String uniId;
    private String cardDetails;
    private List<Long> enrollmentIds = new ArrayList<>();
    private List<Long> borrowRecordIds = new ArrayList<>();
    private List<Long> bookingIds = new ArrayList<>();
}
