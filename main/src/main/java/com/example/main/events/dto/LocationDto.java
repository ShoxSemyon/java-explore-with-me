package com.example.main.events.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class LocationDto {

    Double lat;

    Double lon;
}
