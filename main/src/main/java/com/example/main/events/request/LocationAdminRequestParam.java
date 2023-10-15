package com.example.main.events.request;

import com.example.main.events.dto.LocationType;
import lombok.*;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Data
@Builder
public class LocationAdminRequestParam {

    Double minRadius;

    Double maxRadius;

    LocationType type;

    Double lat;

    Double lon;

    Double distance;

    Integer from;

    Integer size;
}
