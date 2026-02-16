package com.educationalSystem.repository;

import com.educationalSystem.entity.parts.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
