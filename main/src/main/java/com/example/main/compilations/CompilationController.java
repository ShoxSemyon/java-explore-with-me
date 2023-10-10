package com.example.main.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class CompilationController {

    private final CompilationService compilationService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(
            @RequestBody @Validated({NotNullCom.class, Default.class}) NewCompilationDto newCompilationDto) {

        log.info("Сохранение CompilationDto = {}", newCompilationDto);

        return compilationService.create(newCompilationDto);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto update(@RequestBody @Valid NewCompilationDto newCompilationDto,
                                 @PathVariable @NotNull Long compId) {

        log.info("Обновление CompilationDto = {}", newCompilationDto);

        return compilationService.update(newCompilationDto, compId);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull Long compId) {

        log.info("Удаление CompilationDto с id = {}", compId);

        compilationService.delete(compId);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto get(@PathVariable @NotNull Long compId) {

        log.info("Чтение CompilationDto с id = {}", compId);

        return compilationService.get(compId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {

        log.info("Чтение всех CompilationDto ");

        return compilationService.getAll(pinned, from, size);
    }
}
