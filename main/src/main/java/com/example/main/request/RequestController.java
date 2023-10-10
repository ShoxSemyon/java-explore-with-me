package com.example.main.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto createRequest(
            @RequestParam @NotNull Long eventId,
            @PathVariable @NotNull Long userId) {

        log.info("Сохранение Запроса");

        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public RequestDto cancelRequest(
            @PathVariable @NotNull Long requestId,
            @PathVariable @NotNull Long userId) {

        log.info("Отмена Запроса с id={}", requestId);

        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping("/users/{userId}/requests")
    public List<RequestDto> getAllRequestForPublic(
            @PathVariable @NotNull Long userId) {

        log.info("Запрос request");

        return requestService.getAllRequestForPublic(userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getAllRequestForPublic(
            @PathVariable @NotNull Long userId,
            @PathVariable @NotNull Long eventId) {

        log.info("Запрос request по событию с id ={}", eventId);

        return requestService.getAllRequestForUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto requestsConfirmation(
            @PathVariable @NotNull Long eventId,
            @PathVariable @NotNull Long userId,
            @RequestBody @Validated EventRequestStatusUpdateRequestDto dto) {

        log.info("Отмена Запроса с id={}", eventId);

        return requestService.requestsConfirmation(userId, eventId, dto);
    }
}
