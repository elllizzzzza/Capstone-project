package com.educationalSystem.dto;

import com.educationalSystem.enums.RoomType;
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class RoomDTO {

    @NotNull
    private Long roomId;

    @NotNull(message = "Room number is required")
    @Min(value = 1, message = "Room number must be at least 1")
    @Max(value = 9999, message = "Room number cannot exceed 9999")
    private Integer roomNumber;

    @NotNull(message = "Floor is required")
    @Min(value = -5, message = "Floor cannot be below -5 (basement)")
    @Max(value = 200, message = "Floor cannot exceed 200")
    private Integer floor;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 1000, message = "Capacity cannot exceed 1000")
    private Integer capacity;

    @NotNull(message = "Room type is required")
    private RoomType type;
}