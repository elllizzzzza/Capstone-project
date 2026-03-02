package com.educationalSystem.mapper;

import com.educationalSystem.dto.RoomDTO;
import com.educationalSystem.entity.parts.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper implements Mapper<Room, RoomDTO> {

    @Override
    public RoomDTO mapToDTO(Room entity, RoomDTO dto) {
        if (entity == null) return null;

        dto.setRoomId(entity.getRoomId());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setFloor(entity.getFloor());
        dto.setCapacity(entity.getCapacity());
        dto.setType(entity.getType());

        return dto;
    }

    @Override
    public Room mapToEntity(RoomDTO dto, Room entity) {
        if (dto == null) return null;

        entity.setRoomNumber(dto.getRoomNumber());
        entity.setFloor(dto.getFloor());
        entity.setCapacity(dto.getCapacity());
        entity.setType(dto.getType());

        return entity;
    }
}
