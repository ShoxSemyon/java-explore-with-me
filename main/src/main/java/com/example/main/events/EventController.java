package com.example.main.events;

import com.example.main.events.dto.*;
import com.example.main.events.model.EventStatus;
import com.example.main.events.request.EventAdminRequestParam;
import com.example.main.events.request.EventPublicRequestParam;
import com.example.main.events.request.LocationAdminRequestParam;
import com.example.main.events.request.SortEvent;
import com.example.main.exception.InvalidParamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
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
            @RequestBody @Validated({NotNullMarker.class,
                    EventBeginMarker.class,
                    Default.class}) NewEventDto newEventDto,
            @PathVariable @NotNull Long userId) {

        log.info("Сохранение EventDto = {}", newEventDto);

        return eventService.create(newEventDto, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateUser(
            @RequestBody @Validated({EventBeginMarker.class, Default.class}) NewEventDto newEventDto,
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
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double distance,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        log.info("Запрос событий для администратора");
        chekCoordinates(lat, lon, distance);

        return eventService.getAllForAdmin(new EventAdminRequestParam(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size,
                lat,
                lon,
                distance
        ));
    }

    @GetMapping("/admin/events/locations")
    public List<LocationDto> getAllLocationForAdmin(
            @RequestParam(required = false) Double minRadius,
            @RequestParam(required = false) Double maxRadius,
            @RequestParam(required = false) LocationType type,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double distance,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        if (maxRadius != null && minRadius != null) {
            if (minRadius > maxRadius)
                throw new InvalidParamException("minRadius should not be greater than maxRadius");
        }
        chekCoordinates(lat, lon, distance);

        log.info("Запрос локаций для события администратором");

        return eventService.getAllLocation(new LocationAdminRequestParam(
                minRadius,
                maxRadius,
                type,
                lat,
                lon,
                distance,
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
                                               @RequestParam(required = false) Double lat,
                                               @RequestParam(required = false) Double lon,
                                               @RequestParam(required = false) Double distance,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {

        if (rangeEnd == null && rangeStart == null) rangeStart = LocalDateTime.now();
        if ((rangeEnd != null && rangeStart != null)&& rangeStart.isAfter(rangeEnd))
            throw new InvalidParamException("rangeStart must be begin rangeEnd");

        chekCoordinates(lat, lon, distance);


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
                size,
                lat,
                lon,
                distance
        ));
    }

    private void chekCoordinates(Double lat, Double lon, Double distance) {
        if (lat != null || lon != null || distance != null) {
            if (lat == null || lon == null || distance == null) {
                throw new InvalidParamException("If at least 1 parameter lat,long,distance is set," +
                        " then the other two should not be equal to 0");
            }
            if (distance == 0.0) {
                throw new InvalidParamException("distance must be greater than 0");
            }
        }
    }
}
