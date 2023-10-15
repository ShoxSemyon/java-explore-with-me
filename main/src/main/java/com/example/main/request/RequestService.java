package com.example.main.request;

import com.example.demo.client.StatClient;
import com.example.main.events.EventRepository;
import com.example.main.events.EventService;
import com.example.main.events.model.Event;
import com.example.main.events.model.EventStatus;
import com.example.main.exception.NotFoundException;
import com.example.main.exception.UnavailableException;
import com.example.main.users.User;
import com.example.main.users.UserRepository;
import com.example.main.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Import(StatClient.class)
public class RequestService {
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> UserService.createNotFoundException(userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> EventService.createNotFoundException(eventId));

        chekEvent(requester, event);

        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .build();

        setStatusCreateRequest(request, event);

        if (event.getParticipantLimit() > 0 && request.getStatus().equals(RequestStatus.CONFIRMED))
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);

        return requestMapper.convertToRequestDto(requestRepository
                .save(request));
    }

    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findByIdAndAndRequester_Id(requestId,
                userId).orElseThrow(() -> EventService
                .createNotFoundRequestException(requestId));

        Event event = request.getEvent();

        if (request.getStatus().equals(RequestStatus.REJECTED)
                || request.getStatus().equals(RequestStatus.CANCELED))
            return requestMapper.convertToRequestDto(request);

        request.setStatus(RequestStatus.CANCELED);

        if (event.getParticipantLimit() > 0 && request.getStatus().equals(RequestStatus.CONFIRMED))
            request.getEvent().setConfirmedRequests(event.getConfirmedRequests() - 1);

        return requestMapper.convertToRequestDto(request);
    }

    public List<RequestDto> getAllRequestForPublic(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> UserService.createNotFoundException(userId));

        return requestRepository.findAllByRequester_id(userId)
                .stream()
                .map(requestMapper::convertToRequestDto)
                .collect(Collectors.toList());
    }

    public List<RequestDto> getAllRequestForUser(Long userId, Long eventId) {
        List<Request> request = requestRepository.findAllByEvent_Initiator_IdAndEvent_Id(userId, eventId);

        return request.stream()
                .map(requestMapper::convertToRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRequestStatusUpdateResultDto requestsConfirmation(Long userId, Long eventId, EventRequestStatusUpdateRequestDto dto) {

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> EventService.createNotFoundException(eventId));

        List<Request> request = requestRepository.findAllByEvent_Initiator_IdAndEvent_IdAndIdIn(
                userId,
                eventId,
                dto.getRequestIds());

        chekRequest(request, event);

        setStatusRequestForConfirmation(request, event, dto.getStatus());

        return injectResultRequest(request);

    }


    private EventRequestStatusUpdateResultDto injectResultRequest(List<Request> requests) {
        EventRequestStatusUpdateResultDto result = EventRequestStatusUpdateResultDto.builder()
                .rejectedRequests(new ArrayList<>())
                .confirmedRequests(new ArrayList<>())
                .build();
        if (CollectionUtils.isEmpty(requests)) return result;

        for (Request request : requests) {
            if (request.getStatus().equals(RequestStatus.REJECTED))
                result.getRejectedRequests().add(requestMapper.convertToRequestDto(request));
            if (request.getStatus().equals(RequestStatus.CONFIRMED))
                result.getConfirmedRequests().add(requestMapper.convertToRequestDto(request));
        }
        return result;
    }

    private void setStatusRequestForConfirmation(List<Request> requests,
                                                 Event event,
                                                 UpadateRequestStatus status) {
        if (CollectionUtils.isEmpty(requests)
                || !event.getRequestModeration()
                || event.getParticipantLimit() == 0) return;

        int count = event.getParticipantLimit() - event.getConfirmedRequests();

        Integer acc = 0;

        for (Request request : requests) {
            if (status.equals(UpadateRequestStatus.CONFIRMED)
                    && count > 0) {
                request.setStatus(RequestStatus.CONFIRMED);
                acc++;
            } else {
                request.setStatus(RequestStatus.REJECTED);
            }
        }

        event.setConfirmedRequests(event.getConfirmedRequests() + acc);
    }

    private void chekRequest(List<Request> request,
                             Event event) {
        if (CollectionUtils.isEmpty(request)) return;


        if (event.getParticipantLimit() > 0
                && event.getParticipantLimit() - event.getConfirmedRequests() <= 0)
            throw new UnavailableException("The participant limit has been reached");

        request.forEach(request1 -> {
            if (!request1.getStatus().equals(RequestStatus.PENDING))
                throw new UnavailableException("Request must have status PENDING");
        });
    }

    private void setStatusCreateRequest(Request request, Event event) {

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }


    }

    private void chekEvent(User requester, Event event) {
        if (requester.getId() == event.getInitiator().getId())
            throw new UnavailableException("Запрос не может быть от инициатора события");

        if (!event.getState().equals(EventStatus.PUBLISHED))
            throw new UnavailableException("Event must have status PUBLISHED");

        if (event.getParticipantLimit() > 0
                && event.getParticipantLimit() - event.getConfirmedRequests() <= 0)
            throw new UnavailableException("The participant limit has been reached");
    }

    public static NotFoundException createNotFoundRequestException() {
        return new NotFoundException("Requests was not found");
    }
}