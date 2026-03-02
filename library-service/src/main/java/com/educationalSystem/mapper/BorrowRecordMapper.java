package com.educationalSystem.mapper;

import com.educationalSystem.dto.BorrowRecordDTO;
import com.educationalSystem.entity.parts.BorrowRecord;
import org.springframework.stereotype.Component;

@Component
public class BorrowRecordMapper implements Mapper<BorrowRecord, BorrowRecordDTO> {

    @Override
    public BorrowRecordDTO mapToDTO(BorrowRecord entity, BorrowRecordDTO dto) {
        if (entity == null) return null;

        dto.setBorrowRecordId(entity.getBorrowRecordId());
        dto.setBookId(entity.getBook() != null ? entity.getBook().getId() : null);
        dto.setBorrowDate(entity.getBorrowDate());
        dto.setDueDate(entity.getDueDate());
        dto.setEndDate(entity.getEndDate());
        dto.setOverdue(entity.isOverdue());

        return dto;
    }

    @Override
    public BorrowRecord mapToEntity(BorrowRecordDTO dto, BorrowRecord entity) {
        if (dto == null) return null;

        entity.setBorrowDate(dto.getBorrowDate());
        entity.setDueDate(dto.getDueDate());
        entity.setEndDate(dto.getEndDate());
        return entity;
    }
}
