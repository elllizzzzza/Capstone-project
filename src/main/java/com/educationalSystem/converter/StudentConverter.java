package com.educationalSystem.converter;

import com.educationalSystem.dto.StudentDTO;
import com.educationalSystem.entity.parts.BorrowRecord;
import com.educationalSystem.entity.parts.Enrollment;
import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.entity.user.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentConverter implements Converter<Student, StudentDTO> {

    private final UserConverter userConverter;

    @Override
    public StudentDTO convertToDTO(Student entity, StudentDTO dto) {
        if (entity == null) return null;

        userConverter.convertToDTO(entity, dto);

        dto.setUniversity(entity.getUniversity());
        dto.setUniId(entity.getUniId());
        dto.setCardDetails(entity.getCardDetails());

        if (entity.getEnrollments() != null)
            dto.setEnrollmentIds(entity.getEnrollments().stream()
                    .map(Enrollment::getId)
                    .toList());

        if (entity.getBorrowHistory() != null)
            dto.setBorrowRecordIds(entity.getBorrowHistory().stream()
                    .map(BorrowRecord::getBorrowRecordId)
                    .toList());

        if (entity.getBookings() != null)
            dto.setBookingIds(entity.getBookings().stream()
                    .map(RoomBooking::getBookingId)
                    .toList());

        return dto;
    }

    @Override
    public Student convertToEntity(StudentDTO dto, Student entity) {
        if (dto == null) return null;

        userConverter.convertToEntity(dto, entity);

        entity.setUniversity(dto.getUniversity());
        entity.setUniId(dto.getUniId());
        entity.setCardDetails(dto.getCardDetails());

        return entity;
    }
}
