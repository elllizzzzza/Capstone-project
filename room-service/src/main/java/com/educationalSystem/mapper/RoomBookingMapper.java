package com.educationalSystem.mapper;

import com.educationalSystem.dto.RoomBookingDTO;
import com.educationalSystem.entity.parts.RoomBooking;
import org.springframework.stereotype.Component;

@Component
public class RoomBookingMapper implements Mapper<RoomBooking, RoomBookingDTO> {

    @Override
    public RoomBookingDTO mapToDTO(RoomBooking entity, RoomBookingDTO dto) {
        if (entity == null) return null;

        dto.setBookingId(entity.getBookingId());
        dto.setStudentId(entity.getStudentId());
        dto.setRoomId(entity.getRoom() != null ? entity.getRoom().getRoomId() : null);
        dto.setDate(entity.getDate());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setCanceledAt(entity.getCanceledAt());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    @Override
    public RoomBooking mapToEntity(RoomBookingDTO dto, RoomBooking entity) {
        if (dto == null) return null;
        entity.setDate(dto.getDate());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setCanceledAt(dto.getCanceledAt());

        return entity;
    }
}
