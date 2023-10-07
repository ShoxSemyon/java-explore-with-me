package com.example.main.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    public CategoryDto convertToCategoryDto(Category category);

    public Category convertToCategory(CategoryDto categoryDto);
}
