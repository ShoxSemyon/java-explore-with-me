package com.example.main.events.mapper;

import com.example.main.events.dto.EventFullDto;
import com.example.main.events.dto.EventShortDto;
import com.example.main.events.dto.NewEventDto;
import com.example.main.events.model.Event;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "views", ignore = true)
    EventFullDto convertToEventDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    void updateEventFromNewEventDto(NewEventDto newEventDto, @MappingTarget Event event);

    @Mapping(target = "views", ignore = true)
    EventShortDto convertToEventShortDto(Event event);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "confirmedRequests", ignore = true)
    Event convertToEvent(NewEventDto newEventDto);
}
