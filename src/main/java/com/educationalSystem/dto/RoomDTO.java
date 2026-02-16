package com.educationalSystem.dto;

import com.educationalSystem.enums.RoomType;
import lombok.Data;

@Data
public class RoomDTO {
    private Long roomId;
    private int roomNUmber;
    private int floor;
    private int capacity;
    private RoomType type;
}
