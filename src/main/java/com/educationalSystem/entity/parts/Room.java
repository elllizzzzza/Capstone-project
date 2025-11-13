package com.educationalSystem.entity.parts;

import com.educationalSystem.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    private Long roomId;
    private int roomNUmber;
    private int floor;
    private int capacity;
    private RoomType type;
    private boolean available;
}
