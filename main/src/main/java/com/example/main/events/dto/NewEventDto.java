package com.example.main.events.dto;

import com.example.main.events.validator.ValidateEventDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class NewEventDto {

    @NotNull(groups = NotNullMarker.class)
    @Size(max = 2000, min = 20)
    String annotation;

    @NotNull(groups = NotNullMarker.class)
    Long category;

    @NotNull(groups = NotNullMarker.class)
    @Size(max = 7000, min = 20)
    String description;

    @NotNull(groups = NotNullMarker.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ValidateEventDate(groups = EventBeginMarker.class)
    LocalDateTime eventDate;

    @NotNull(groups = NotNullMarker.class)
    LocationDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    @NotNull(groups = NotNullMarker.class)
    @Size(max = 7000, min = 20)
    String title;

    StateAction stateAction;

}
