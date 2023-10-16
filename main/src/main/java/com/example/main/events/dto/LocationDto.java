package com.example.main.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class LocationDto {

    @NonNull
    Double lat;
    @NotNull
    Double lon;

    Double radius = 0.0;

    LocationType type = LocationType.PLACE;
}
