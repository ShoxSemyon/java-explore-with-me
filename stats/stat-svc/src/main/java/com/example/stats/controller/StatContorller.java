package com.example.stats.controller;

import com.example.stats.dto.EndpointHitDto;
import com.example.stats.dto.ViewStatsDto;
import com.example.stats.exception.DateNotValidException;
import com.example.stats.service.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatContorller {

    private final StatService statService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info("Начало сохранение статистики {}",
                endpointHitDto);

        return statService.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getAll(@RequestParam(defaultValue = "false") Boolean unique,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                     @RequestParam(required = false) List<String> uris) {

        log.info("Запрос статистики");

        if (start.isAfter(end)) throw new DateNotValidException("start date is after end date");

        return statService.get(start, end, uris, unique);
    }

}
