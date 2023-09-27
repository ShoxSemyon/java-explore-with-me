package com.example.stats.service;

import com.example.stats.dto.EndpointHitDto;
import com.example.stats.dto.ViewStatsDto;
import com.example.stats.mapper.StatMapper;
import com.example.stats.mapper.ViewStatMapper;
import com.example.stats.model.EndpointHit;
import com.example.stats.model.ViewStats;
import com.example.stats.repository.StatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatService {
    private final StatRepository statRepository;

    private final StatMapper mapper;

    @Transactional
    public EndpointHitDto create(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = mapper.convertToEndpointHit(endpointHitDto);

        endpointHit = statRepository.save(endpointHit);

        return mapper.convertToEndpointHitDto(endpointHit);
    }

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        List<ViewStats> viewStatsList;
        if (unique) {
            viewStatsList = CollectionUtils.isEmpty(uris) ?
                    statRepository.findAllUnique(start, end)
                    :
                    statRepository.findAllUnique(start, end, uris);
        } else {
            viewStatsList = CollectionUtils.isEmpty(uris) ?
                    statRepository.findAll(start, end)
                    :
                    statRepository.findAll(start, end, uris);
        }
        return viewStatsList.stream()
                .map(ViewStatMapper::convertToViewStatsDto)
                .collect(Collectors.toList());
    }
}
