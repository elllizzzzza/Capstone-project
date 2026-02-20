package com.educationalSystem.mapper;

import com.educationalSystem.dto.RoomDTO;
import com.educationalSystem.entity.parts.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper implements Converter<Room, RoomDTO> {

    @Override
    public RoomDTO convertToDTO(Room entity, RoomDTO dto) {
        if (entity == null) return null;

        dto.setRoomId(entity.getRoomId());
        dto.setRoomNUmber(entity.getRoomNumber());
        dto.setFloor(entity.getFloor());
        dto.setCapacity(entity.getCapacity());
        dto.setType(entity.getType());

        return dto;
    }

    @Override
    public Room convertToEntity(RoomDTO dto, Room entity) {
        if (dto == null) return null;

        entity.setRoomNumber(dto.getRoomNUmber());
        entity.setFloor(dto.getFloor());
        entity.setCapacity(dto.getCapacity());
        entity.setType(dto.getType());

        return entity;
    }
}
