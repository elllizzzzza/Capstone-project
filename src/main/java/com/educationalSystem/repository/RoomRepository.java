package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
