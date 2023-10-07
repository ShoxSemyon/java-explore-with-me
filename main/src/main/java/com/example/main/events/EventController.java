package com.example.main.events;

import com.example.main.events.dto.*;
import com.example.main.events.model.EventStatus;
import com.example.main.events.request.EventAdminRequestParam;
import com.example.main.events.request.EventPublicRequestParam;
import com.example.main.events.request.SortEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(
            @RequestBody @Validated({NotNullMarker.class, EventBeginMarker.class}) NewEventDto newEventDto,
            @PathVariable @NotNull Long userId) {

        log.info("Сохранение EventDto = {}", newEventDto);

        return eventService.create(newEventDto, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateUser(@RequestBody @Validated({EventBeginMarker.class}) NewEventDto newEventDto,
                                   @PathVariable @NotNull Long userId,
                                   @PathVariable @NotNull Long eventId) {

        log.info("Обновление EventDto пользователем = {}", newEventDto);

        return eventService.updateUser(newEventDto, eventId, userId);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateAdmin(@RequestBody @Valid NewEventDto newEventDto,
                                    @PathVariable @NotNull Long eventId) {

        log.info("Обновление EventDto админом = {}", newEventDto);

        return eventService.updateAdmin(newEventDto, eventId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAllForUser(@PathVariable @NotNull Long userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {

        log.info("Запрос списка событий для пользователя");

        return eventService.getAllForUser(userId, from, size);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getForUser(@PathVariable @NotNull Long userId,
                                   @PathVariable @NotNull Long eventId) {

        log.info("Запрос события={} для пользователя", eventId);

        return eventService.getForUser(eventId, userId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getAllForAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventStatus> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        log.info("Запрос событий для администратора");

        return eventService.getAllForAdmin(new EventAdminRequestParam(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size
        ));
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getForPublic(@PathVariable @NotNull Long eventId) {

        log.info("Запрос события={}", eventId);

        return eventService.getForPublic(eventId);
    }

    @GetMapping("/events")
    public List<EventShortDto> getAllForPublic(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) List<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(required = false) SortEvent sort,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {

        if (rangeEnd == null && rangeStart == null) rangeStart = LocalDateTime.now();


        log.info("Запрос событий");

        return eventService.getAllForPublic(new EventPublicRequestParam(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size
        ));
    }

}
