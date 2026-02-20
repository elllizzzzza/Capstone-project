package com.educationalSystem.service;

import com.educationalSystem.mapper.RoomMapper;
import com.educationalSystem.dto.RoomDTO;
import com.educationalSystem.entity.parts.Room;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(r -> roomMapper.convertToDTO(r, new RoomDTO()))
                .toList();
    }

    public RoomDTO getRoomById(Long id) {
        Room room = findOrThrow(id);
        return roomMapper.convertToDTO(room, new RoomDTO());
    }

    @Transactional
    public RoomDTO createRoom(RoomDTO dto) {
        Room room = roomMapper.convertToEntity(dto, new Room());
        roomRepository.save(room);
        return roomMapper.convertToDTO(room, new RoomDTO());
    }

    @Transactional
    public RoomDTO updateRoom(Long id, RoomDTO dto) {
        Room room = findOrThrow(id);
        roomMapper.convertToEntity(dto, room);
        roomRepository.save(room);
        return roomMapper.convertToDTO(room, new RoomDTO());
    }

    @Transactional
    public void deleteRoom(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new ResourceNotFoundException("Room not found: " + id);
        }
        roomRepository.deleteById(id);
    }

    private Room findOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }
}