package com.educationalSystem.converter;

import com.educationalSystem.dto.LibrarianDTO;
import com.educationalSystem.entity.user.Librarian;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibrarianConverter implements Converter<Librarian, LibrarianDTO> {

    private final UserConverter userConverter;

    @Override
    public LibrarianDTO convertToDTO(Librarian entity, LibrarianDTO dto) {
        if (entity == null) {
            return null;
        }

        userConverter.convertToDTO(entity, dto);

        return dto;
    }

    @Override
    public Librarian convertToEntity(LibrarianDTO dto, Librarian entity) {
        if (dto == null) {
            return null;
        }

        userConverter.convertToEntity(dto, entity);

        return entity;
    }
}
