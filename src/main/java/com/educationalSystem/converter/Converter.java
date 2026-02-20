package com.educationalSystem.converter;

public interface Converter<E, D> {

    D convertToDTO(E entity, D dto);

    E convertToEntity (D dto, E entity);
}