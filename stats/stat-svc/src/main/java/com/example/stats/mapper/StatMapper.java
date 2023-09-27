package com.example.stats.mapper;

import com.example.stats.dto.EndpointHitDto;
import com.example.stats.model.EndpointHit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StatMapper {
    @Mapping(target = "createdAt",source = "endpointHitDto.timestamp")
    EndpointHit convertToEndpointHit(EndpointHitDto endpointHitDto);

    @Mapping(target = "timestamp",source = "endpointHit.createdAt")
    EndpointHitDto convertToEndpointHitDto(EndpointHit endpointHit);
}
