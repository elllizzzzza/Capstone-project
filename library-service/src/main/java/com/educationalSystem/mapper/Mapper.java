package com.educationalSystem.mapper;

public interface Mapper<E, D> {

    D mapToDTO(E entity, D dto);

    E mapToEntity(D dto, E entity);
}