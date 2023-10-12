package com.example.main.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid CategoryDto categoryDto) {
        log.info("Сохранение categoryDto = {}", categoryDto);

        return categoryService.create(categoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull Long categoryId) {
        log.info("Удаление категории с id = {}", categoryId);

        categoryService.delete(categoryId);
    }

    @PatchMapping("/admin/categories/{categoryId}")
    public CategoryDto update(@RequestBody @Valid CategoryDto categoryDto,
                              @PathVariable @NotNull Long categoryId) {

        log.info("Обновление categoryDto = {} с id ={}",
                categoryDto,
                categoryId);

        return categoryService.update(categoryDto, categoryId);
    }

    @GetMapping("/categories/{categoryId}")
    public CategoryDto get(@PathVariable @NotNull Long categoryId) {

        log.info("Запрос categoryDto  с id ={}", categoryId);

        return categoryService.get(categoryId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> get(@RequestParam(defaultValue = "0") Integer from,
                                 @RequestParam(defaultValue = "10") Integer size) {

        log.info("Запрос списка categoryDto");

        return categoryService.getAll(from, size);
    }
}
