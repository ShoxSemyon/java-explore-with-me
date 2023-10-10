package com.example.main.events.dto;

import com.example.main.events.validator.ValidateEventDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class NewEventDto {

    @NotBlank(groups = NotNullMarker.class)
    @Size(max = 2000, min = 20)
    String annotation;

    @NotNull(groups = NotNullMarker.class)
    Long category;

    @NotBlank(groups = NotNullMarker.class)
    @Size(max = 7000, min = 20)
    String description;

    @NotNull(groups = NotNullMarker.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @ValidateEventDate(groups = EventBeginMarker.class)
    @FutureOrPresent
    LocalDateTime eventDate;

    @NotNull(groups = NotNullMarker.class)
    LocationDto location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    @NotBlank(groups = NotNullMarker.class)
    @Size(max = 120, min = 3)
    String title;

    StateAction stateAction;

}
