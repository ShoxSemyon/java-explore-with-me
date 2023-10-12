package com.example.main.category;

import com.example.main.exception.NotFoundException;
import com.example.main.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {

        return categoryMapper.convertToCategoryDto(categoryRepository
                .save(categoryMapper
                        .convertToCategory(categoryDto)
                ));
    }

    @Transactional
    public CategoryDto update(CategoryDto categoryDto, Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> createNotFoundException(categoryId));

        Category category = categoryMapper.convertToCategory(categoryDto);
        category.setId(categoryId);

        return categoryMapper.convertToCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long categoryId) {

        categoryRepository.findById(categoryId)
                .orElseThrow(() -> createNotFoundException(categoryId));

        categoryRepository.deleteById(categoryId);
    }

    public CategoryDto get(Long categoryId) {

        return categoryMapper.convertToCategoryDto(categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> createNotFoundException(categoryId)));
    }

    public List<CategoryDto> getAll(int from, int size) {

        return categoryRepository
                .findAll(new OffsetBasedPageRequest(from, size))
                .stream()
                .map(categoryMapper::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    public static NotFoundException createNotFoundException(Long categoryId) {
        return new NotFoundException(String
                .format("Category with id=%s was not found",
                        categoryId));
    }
}
