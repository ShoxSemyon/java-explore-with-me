package com.example.main.events.dto;

import com.example.main.category.CategoryDto;
import com.example.main.events.model.EventStatus;
import com.example.main.users.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class EventFullDto {

    long id;

    String annotation;

    CategoryDto category;

    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    LocationDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    String title;

    Integer confirmedRequests;

    LocalDateTime createdOn;

    UserDto initiator;

    LocalDateTime publishedOn;

    EventStatus state;

    Long views;
}
