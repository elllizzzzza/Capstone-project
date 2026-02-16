package com.educationalSystem.converter;

import com.educationalSystem.dto.BookDTO;
import com.educationalSystem.entity.parts.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter implements Converter<Book, BookDTO> {

    @Override
    public BookDTO convertToDTO(Book entity, BookDTO dto) {
        if (entity == null) return null;

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setAuthor(entity.getAuthor());
        dto.setLanguage(entity.getLanguage());
        dto.setGenre(entity.getGenre());

        return dto;
    }

    @Override
    public Book convertToEntity(BookDTO dto, Book entity) {
        if (dto == null) return null;

        entity.setTitle(dto.getTitle());
        entity.setAuthor(dto.getAuthor());
        entity.setLanguage(dto.getLanguage());
        entity.setGenre(dto.getGenre());

        return entity;
    }
}
