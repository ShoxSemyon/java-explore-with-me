package com.example.main.events;

import com.example.demo.client.StatClient;
import com.example.main.category.CategoryRepository;
import com.example.main.category.CategoryService;
import com.example.main.events.dto.EventFullDto;
import com.example.main.events.dto.EventShortDto;
import com.example.main.events.dto.NewEventDto;
import com.example.main.events.dto.StateAction;
import com.example.main.events.mapper.EventMapper;
import com.example.main.events.model.Event;
import com.example.main.events.model.EventStatus;
import com.example.main.events.model.QEvent;
import com.example.main.events.request.EventAdminRequestParam;
import com.example.main.events.request.EventPublicRequestParam;
import com.example.main.events.request.SortEvent;
import com.example.main.exception.EventDateException;
import com.example.main.exception.EventStatusException;
import com.example.main.exception.NotFoundException;
import com.example.main.users.UserRepository;
import com.example.main.users.UserService;
import com.example.main.utils.EventDateComparable;
import com.example.main.utils.EventViewComparable;
import com.example.main.utils.OffsetBasedPageRequest;
import com.example.stats.dto.EndpointHitDto;
import com.example.stats.dto.ViewStatsDto;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Import(StatClient.class)
public class EventService {
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final StatClient statClient;

    @Transactional
    public EventFullDto create(NewEventDto newEventDto, Long userId) {

        Event event = eventMapper.convertToEvent(newEventDto);

        setCategory(newEventDto, event);

        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> UserService.createNotFoundException(userId)));

        setDefaultValue(event);


        return eventMapper.convertToEventDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto updateUser(NewEventDto newEventDto, Long eventId, Long userId) {

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> createNotFoundException(eventId));

        if (event.getState().equals(EventStatus.PUBLISHED))
            throw new EventStatusException("Only pending or canceled events can be changed");

        eventMapper.updateEventFromNewEventDto(newEventDto, event);

        setCategory(newEventDto, event);

        setStatus(newEventDto, event);

        return eventMapper.convertToEventDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto updateAdmin(NewEventDto newEventDto, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> createNotFoundException(eventId));

        validateStatus(newEventDto, event);

        checkPublishDate(newEventDto, event);

        eventMapper.updateEventFromNewEventDto(newEventDto, event);

        setCategory(newEventDto, event);

        setStatus(newEventDto, event);

        event = eventRepository.save(event);

        //TODO add view

        return eventMapper.convertToEventDto(event);
    }

    public List<EventShortDto> getAllForUser(Long userId, Integer from, Integer size) {

        //TODO add view
        return eventRepository.findAllByInitiator_id(userId,
                        new OffsetBasedPageRequest(from, size))
                .stream()
                .map(eventMapper::convertToEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getForUser(Long eventId, Long userId) {

        Event event = eventRepository.findByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> createNotFoundException(eventId));


        //TODO add view

        return eventMapper.convertToEventDto(event);
    }

    public List<EventFullDto> getAllForAdmin(EventAdminRequestParam eventAdminRequestParam) {
        if (eventAdminRequestParam.usersStatesCategoriesStartEndIsNull()) {
            return eventRepository.findAll(new OffsetBasedPageRequest(
                            eventAdminRequestParam.getFrom(),
                            eventAdminRequestParam.getSize()))
                    .stream()
                    .map(eventMapper::convertToEventDto)
                    .collect(Collectors.toList());
        }
        BooleanBuilder booleanBuilder = getPredicateForAdminQuery(eventAdminRequestParam);

        return eventRepository.findAll(booleanBuilder, new OffsetBasedPageRequest(
                        eventAdminRequestParam.getFrom(),
                        eventAdminRequestParam.getSize()))
                .stream()
                .map(eventMapper::convertToEventDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getForPublic(Long eventId) {

        Event event = eventRepository.findByIdAndState(eventId, EventStatus.PUBLISHED)
                .orElseThrow(() -> createNotFoundException(eventId));

        EventFullDto eventFullDto = eventMapper.convertToEventDto(event);

        setViewForEventFullDto(
                event.getCreatedOn(),
                LocalDateTime.now(),
                List.of("/events/" + eventId, "/events/4"),
                eventFullDto);

        sendHit("/events/" + eventId);
        return eventFullDto;
    }

    public List<EventShortDto> getAllForPublic(EventPublicRequestParam eventPublicRequestParam) {
        BooleanBuilder booleanBuilder = getPredicateForPublicQuery(eventPublicRequestParam);

        List<Event> events = eventRepository.findAll(booleanBuilder, new OffsetBasedPageRequest(
                        eventPublicRequestParam.getFrom(),
                        eventPublicRequestParam.getSize()))
                .stream()
                .collect(Collectors.toList());

        List<EventShortDto> eventShortDtos = events
                .stream()
                .map(eventMapper::convertToEventShortDto)
                .collect(Collectors.toList());

        String uri = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
        sendHit(uri.substring(uri.indexOf("/", 10)));

        setViewForListEventShortDto(events, eventShortDtos);

        sortEvent(eventPublicRequestParam, eventShortDtos);

        return eventShortDtos;
    }

    private void sortEvent(EventPublicRequestParam eventPublicRequestParam, List<EventShortDto> eventShortDtos) {
        if (eventPublicRequestParam.getSort() != null) {
            eventShortDtos.sort(eventPublicRequestParam.getSort() == SortEvent.EVENT_DATE ?
                    new EventDateComparable() : new EventViewComparable());
        }
    }

    private void setViewForListEventShortDto(List<Event> events, List<EventShortDto> eventShortDtos) {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        List<String> uris = new ArrayList<>();
        for (Event event : events) {
            start = start.isAfter(event.getCreatedOn()) ? event.getCreatedOn() : start;
            end = end.isBefore(event.getEventDate()) ? event.getEventDate() : end;
            uris.add("/events/" + event.getId());
        }
        List<ViewStatsDto> viewStatsDtoList = getViewStatsDtos(false, start, end, uris);
        if (CollectionUtils.isEmpty(viewStatsDtoList)) return;

        Map<Long, ViewStatsDto> viewStatsDtoMap = viewStatsDtoList.stream()
                .collect(Collectors.toMap(viewStatsDto ->
                                Long.parseLong(
                                        viewStatsDto
                                                .getUri()
                                                .substring(viewStatsDto
                                                        .getUri()
                                                        .lastIndexOf("/"))),
                        Function.identity()));


        eventShortDtos.forEach(e -> {
            ViewStatsDto viewStatsDto = viewStatsDtoMap.getOrDefault(e.getId(),
                    null);
            e.setViews(viewStatsDto != null ?
                    viewStatsDto.getHits() : 0);
        });
    }

    private void sendHit(String uri) {


        try {
            statClient.create(EndpointHitDto.builder()
                    .app("main")
                    .uri(uri)
                    .ip(Inet4Address.getLocalHost().getHostAddress())
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);

        }
    }

    private void setViewForEventFullDto(LocalDateTime start, LocalDateTime end, List<String> uris, EventFullDto dto) {
        List<ViewStatsDto> viewStatsDtoList = getViewStatsDtos(true, start, end, uris);

        if (viewStatsDtoList != null) {
            viewStatsDtoList.stream()
                    .findFirst()
                    .ifPresent(viewStatsDto -> dto.setViews(viewStatsDto.getHits()));
        }
    }

    @Nullable
    private List<ViewStatsDto> getViewStatsDtos(Boolean uniq, LocalDateTime start, LocalDateTime end, List<String> uris) {

        return statClient.getAll(
                uniq,
                start,
                end,
                uris).getBody();
    }

    private BooleanBuilder getPredicateForAdminQuery(EventAdminRequestParam eventAdminRequestParam) {
        QEvent qevent = QEvent.event;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!CollectionUtils.isEmpty(eventAdminRequestParam.getUsers()))
            booleanBuilder.and(qevent.initiator.id.in(eventAdminRequestParam.getUsers()));

        if (!CollectionUtils.isEmpty(eventAdminRequestParam.getStates()))
            booleanBuilder.and(qevent.state.in(eventAdminRequestParam.getStates()));

        if (!CollectionUtils.isEmpty(eventAdminRequestParam.getCategories()))
            booleanBuilder.and(qevent.category.id.in(eventAdminRequestParam.getCategories()));

        if (eventAdminRequestParam.getRangeStart() != null)
            booleanBuilder.and(qevent.eventDate.after(eventAdminRequestParam.getRangeStart()));

        if (eventAdminRequestParam.getRangeEnd() != null)
            booleanBuilder.and(qevent.eventDate.before(eventAdminRequestParam.getRangeEnd()));

        return booleanBuilder;
    }

    private BooleanBuilder getPredicateForPublicQuery(EventPublicRequestParam eventPublicRequestParam) {
        QEvent qevent = QEvent.event;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qevent.state.eq(EventStatus.PUBLISHED));

        if (eventPublicRequestParam.getOnlyAvailable())
            booleanBuilder.and(qevent.participantLimit.gt(qevent.confirmedRequests));

        if (eventPublicRequestParam.getPaid() != null)
            booleanBuilder.and(qevent.paid.eq(eventPublicRequestParam.getPaid()));

        if (eventPublicRequestParam.getText() != null)
            booleanBuilder.and(qevent.annotation.likeIgnoreCase(eventPublicRequestParam.getText())
                    .or(qevent.description.likeIgnoreCase(eventPublicRequestParam.getText())));

        if (!CollectionUtils.isEmpty(eventPublicRequestParam.getCategories()))
            booleanBuilder.and(qevent.category.id.in(eventPublicRequestParam.getCategories()));

        if (eventPublicRequestParam.getRangeStart() != null)
            booleanBuilder.and(qevent.eventDate.after(eventPublicRequestParam.getRangeStart()));

        return booleanBuilder;
    }

    private void checkPublishDate(NewEventDto newEventDto, Event event) {

        if (newEventDto.getStateAction() != null
                && newEventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)
                && ((newEventDto.getEventDate() != null &&
                newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) ||
                event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))))
            throw new EventDateException("Field: eventDate. " +
                    "Error: публикация возможна за час до старта события ");
    }


    private void setCategory(NewEventDto newEventDto, Event event) {
        if (newEventDto.getCategory() == null) return;
        event.setCategory(categoryRepository
                .findById(newEventDto
                        .getCategory())
                .orElseThrow(() -> CategoryService.createNotFoundException(newEventDto
                        .getCategory())));
    }

    private void setDefaultValue(Event event) {
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventStatus.PENDING);
        if (event.getParticipantLimit() == null) event.setParticipantLimit(0);
        if (event.getConfirmedRequests() == null) event.setConfirmedRequests(0);
        if (event.getPaid() == null) event.setPaid(false);
        if (event.getRequestModeration() == null) event.setRequestModeration(true);
    }

    private void setStatus(NewEventDto newEventDto, Event event) {
        StateAction action = newEventDto.getStateAction();
        if (newEventDto.getStateAction() != null) {
            switch (action) {
                case SEND_TO_REVIEW:
                    event.setState(EventStatus.PENDING);
                    break;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    event.setState(EventStatus.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(EventStatus.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;

            }
        }
    }

    private void validateStatus(NewEventDto newEventDto, Event event) {
        if (newEventDto.getStateAction() != null) {
            if (newEventDto.getStateAction().equals(StateAction.PUBLISH_EVENT)
                    && !event.getState().equals(EventStatus.PENDING)) {
                throw new EventStatusException("Cannot publish the event because " +
                        "it's not in the right state: PUBLISHED");
            }
            if (newEventDto.getStateAction().equals(StateAction.REJECT_EVENT)
                    && event.getState().equals(EventStatus.PUBLISHED)) {
                throw new EventStatusException("Cannot canceled the event because " +
                        "it's not in the right state: PUBLISHED");
            }

        }
    }

    public static NotFoundException createNotFoundException(Long eventId) {
        return new NotFoundException(String
                .format("Event with id=%s was not found",
                        eventId));
    }

    public static NotFoundException createNotFoundRequestException(Long eventId) {
        return new NotFoundException(String
                .format("Request with id=%s was not found",
                        eventId));
    }


}
