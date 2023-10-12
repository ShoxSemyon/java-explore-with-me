package com.example.main.events.mapper;

import com.example.main.events.dto.LocationDto;
import com.example.main.events.model.Location;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    LocationDto convertToLocationDto(Location location);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateLocationFromLocationDto(LocationDto locationDto, @MappingTarget Location location);

    Location convertToLocation(LocationDto locationDto);
}
