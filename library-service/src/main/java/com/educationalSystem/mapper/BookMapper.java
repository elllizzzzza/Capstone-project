package com.educationalSystem.mapper;

import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.entity.parts.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper implements Mapper<Book, BookDTO> {

    @Override
    public BookDTO mapToDTO(Book entity, BookDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setLanguage(entity.getLanguage());
        dto.setGenre(entity.getGenre());
        dto.setTotalCopies(entity.getTotalCopies());
        dto.setAvailableCopies(entity.getAvailableCopies());

        return dto;
    }

    @Override
    public Book mapToEntity(BookDTO dto, Book entity) {
        if (dto == null) return null;

        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setLanguage(dto.getLanguage());
        entity.setGenre(dto.getGenre());
        entity.setTotalCopies(dto.getTotalCopies());
        entity.setAvailableCopies(dto.getAvailableCopies());

        return entity;
    }
}
