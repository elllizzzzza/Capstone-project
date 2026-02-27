package com.educationalSystem.mapper;

import com.educationalSystem.dto.LibrarianDTO;
import com.educationalSystem.entity.user.Librarian;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibrarianMapper implements Mapper<Librarian, LibrarianDTO> {

    private final UserMapper userMapper;

    @Override
    public LibrarianDTO mapToDTO(Librarian entity, LibrarianDTO dto) {
        if (entity == null) {
            return null;
        }

        userMapper.mapToDTO(entity, dto);

        return dto;
    }

    @Override
    public Librarian mapToEntity(LibrarianDTO dto, Librarian entity) {
        if (dto == null) {
            return null;
        }

        userMapper.mapToEntity(dto, entity);

        return entity;
    }
}
