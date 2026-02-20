package com.educationalSystem.mapper;

import com.educationalSystem.dto.LibrarianDTO;
import com.educationalSystem.entity.user.Librarian;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibrarianMapper implements Converter<Librarian, LibrarianDTO> {

    private final UserMapper userMapper;

    @Override
    public LibrarianDTO convertToDTO(Librarian entity, LibrarianDTO dto) {
        if (entity == null) {
            return null;
        }

        userMapper.convertToDTO(entity, dto);

        return dto;
    }

    @Override
    public Librarian convertToEntity(LibrarianDTO dto, Librarian entity) {
        if (dto == null) {
            return null;
        }

        userMapper.convertToEntity(dto, entity);

        return entity;
    }
}
