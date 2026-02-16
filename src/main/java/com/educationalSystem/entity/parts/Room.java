package com.educationalSystem.entity.parts;

import com.educationalSystem.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private int roomNumber;
    private int floor;
    private int capacity;

    @Enumerated(EnumType.STRING)
    private RoomType type;
}
