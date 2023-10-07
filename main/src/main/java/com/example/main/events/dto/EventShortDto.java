package com.example.main.events.dto;

import com.example.main.category.CategoryDto;
import com.example.main.users.UserShortDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class EventShortDto {

    long id;

    String annotation;

    CategoryDto category;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    Boolean paid;

    String title;

    Integer confirmedRequests;

    UserShortDto initiator;

    Long views;
}
